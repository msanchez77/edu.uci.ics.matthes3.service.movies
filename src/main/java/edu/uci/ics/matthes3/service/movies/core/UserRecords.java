package edu.uci.ics.matthes3.service.movies.core;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.matthes3.service.movies.MovieService;
import edu.uci.ics.matthes3.service.movies.logger.ServiceLogger;
import edu.uci.ics.matthes3.service.movies.models.*;
import edu.uci.ics.matthes3.service.movies.utilities.ResultCodes;
import edu.uci.ics.matthes3.service.movies.utilities.SQLData;
import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.ws.Service;
import java.io.IOException;
import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static edu.uci.ics.matthes3.service.movies.utilities.SQLData.buildArrayFromSQL;
import static edu.uci.ics.matthes3.service.movies.utilities.SQLData.getSubstringText;

public class UserRecords {
    public static boolean userPrivilegeSufficient(String email, int plevel) {
        ServiceLogger.LOGGER.info("Getting privilege level with IDM...");

        // Create a new Client
        ServiceLogger.LOGGER.info("Building client...");
        Client client = ClientBuilder.newClient();
        client.register(JacksonFeature.class);

        // Get the URI for the IDM
        ServiceLogger.LOGGER.info("Building URI...");
        String IDM_URI = MovieService.getMovieConfigs().getIdmConfigs().getIdmUri();

        ServiceLogger.LOGGER.info("Setting path to endpoint...");
        String IDM_ENDPOINT_PATH = MovieService.getMovieConfigs().getIdmConfigs().getPrivilegePath();

        // Create a WebTarget to send a request at
        ServiceLogger.LOGGER.info("Building WebTarget...");
        WebTarget webTarget = client.target(IDM_URI).path(IDM_ENDPOINT_PATH);

        // Create an InvocationBuilder to create the HTTP request
        ServiceLogger.LOGGER.info("Starting invocation builder...");
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

        // Set the payload
        ServiceLogger.LOGGER.info("Setting payload of the request");
        VerifyPrivilegeRequestModel requestModel = new VerifyPrivilegeRequestModel(email, plevel);

        // Send the request and save it to a Response
        ServiceLogger.LOGGER.info("Sending request...");
        Response response = invocationBuilder.post(Entity.entity(requestModel, MediaType.APPLICATION_JSON));
        ServiceLogger.LOGGER.info("Sent!");

        // Check that status code of the request
        if (response.getStatus() == 200) {
            ServiceLogger.LOGGER.info("Received Status 200");
            // Success! Map the response to a ResponseModel
//            VerifyPrivilegeResponseModel responseModel = response.readEntity(VerifyPrivilegeResponseModel.class);
            String jsonText = response.readEntity(String.class);

            ObjectMapper mapper = new ObjectMapper();
            try {
                VerifyPrivilegeResponseModel responseModel = mapper.readValue(jsonText, VerifyPrivilegeResponseModel.class);

                ServiceLogger.LOGGER.info("Verify Privilege ResultCode: " + responseModel.getResultCode());

                return responseModel.getResultCode() == 140;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            ServiceLogger.LOGGER.info("Received Status " + response.getStatus() + " -- you lose.");
        }
        return false;
    }


    public static MovieSearchModel[] retrieveMoviesFromDB(SearchMovieRequestModel requestModel, boolean allowHidden) {
        String title = requestModel.getTitle();
        String genre = requestModel.getGenre();
        Integer year = requestModel.getYear();
        String director = requestModel.getDirector();
        Boolean hidden = requestModel.getHidden();
        Integer limit = requestModel.getLimit();
        Integer offset = requestModel.getOffset();
        String sortBy = requestModel.getOrderby();
        String direction = requestModel.getDirection();

        ServiceLogger.LOGGER.info("Searching for movie...");

        String query;
        query =
                "SELECT DISTINCT m.id, m.title, m.director, m.year, r.rating, r.numVotes, m.hidden " +
                "FROM movies m " +
                    "LEFT OUTER JOIN ratings r on m.id = r.movieId " +
                    "LEFT OUTER JOIN genres_in_movies gim on m.id = gim.movieId " +
                    "LEFT OUTER JOIN genres g on gim.genreId = g.id " +
                "WHERE ((? IS NULL) OR (m.title LIKE ?)) AND " +
                    "((? IS NULL) OR (g.name LIKE ?)) AND " +
                    "((? <= 0) OR (m.year = ?)) AND " +
                    "((? IS NULL) OR (m.director LIKE ?)) ";
        if (!allowHidden)
            query += "AND m.hidden = 0 ";

        query +=
                "GROUP BY m.id " +
                "ORDER BY %s %s, %s %s " +
                "LIMIT ? " +
                "OFFSET ? ";
        try {
            String secondary_sortBy = sortBy.equals("rating") ? "title" : "rating";
            String secondary_direction = direction.equals("desc") ? "asc" : "desc";

            query = String.format(query, sortBy, direction, secondary_sortBy, secondary_direction);

            PreparedStatement ps = MovieService.getCon().prepareStatement(query);

            ps.setString(1, getSubstringText(title));
            ps.setString(2, getSubstringText(title));
            ps.setString(3, getSubstringText(genre));
            ps.setString(4, getSubstringText(genre));
            ps.setInt(5, year);
            ps.setInt(6, year);
            ps.setString(7, getSubstringText(director));
            ps.setString(8, getSubstringText(director));
            ps.setInt(9, limit);
            ps.setInt(10, offset);

            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");

            ArrayList<Movie> movies = new ArrayList<>();
            int num_movies = 0;
            while (rs.next()) {
                String currentTitle = rs.getString("title");
                ServiceLogger.LOGGER.info("Retrieved movie: " + currentTitle);

                int hiddenInt = rs.getInt("hidden");
                Boolean hiddenMovie = (hiddenInt == 1);

                Movie m = new Movie(
                        rs.getString("id"),
                        rs.getString("title"),
                        rs.getString("director"),
                        rs.getInt("year"),
                        rs.getFloat("rating"),
                        rs.getInt("numVotes"),
                        allowHidden ? hiddenMovie : null
                );

                movies.add(m);
                num_movies++;
            }

            ServiceLogger.LOGGER.info("Retrieved " + num_movies + " movies.");
            ServiceLogger.LOGGER.info("Exiting retrieveMoviesFromDB...");
            return Movie.buildMovieSearchArray(movies);

        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed.");
            e.printStackTrace();
        }

        return new MovieSearchModel[0];
    }

    public static MovieModel retrieveMovieIDFromDB(String id, boolean allowHidden) {

        ServiceLogger.LOGGER.info("Searching for a movie ID...");

        String query_all;
        String query_hidden;

        query_all =
                "SELECT DISTINCT m1.id AS id, m1.title, m1.director, m1.year, m1.backdrop_path, m1.budget, m1.overview, m1.poster_path, m1.revenue, " +
                        "r1.rating, r1.numVotes, " +
                        "GROUP_CONCAT(s.id) AS sg_id, GROUP_CONCAT(s.name) AS sg_name, GROUP_CONCAT(IFNULL(s.birthYear, -1)) AS s_year, COUNT(s.id) AS cnt " +
                "FROM ((movies AS m1 " +
                    "LEFT OUTER JOIN ratings AS r1 on m1.id = r1.movieId " +
                    "LEFT OUTER JOIN stars_in_movies sim on r1.movieId = sim.movieId) " +
                    "LEFT OUTER JOIN stars s on sim.starId = s.id) " +
                "WHERE m1.id = ? " +
                "UNION " +
                "SELECT DISTINCT m.id AS id, m.title, m.director, m.year, m.backdrop_path, m.budget, m.overview, m.poster_path, m.revenue, " +
                        "r.rating, r.numVotes, " +
                        "GROUP_CONCAT(g.id) AS sg_id, GROUP_CONCAT(g.name) AS sg_name, 0, COUNT(g.id) " +
                "FROM ((movies AS m " +
                    "INNER JOIN ratings AS r on m.id = r.movieId " +
                    "INNER JOIN genres_in_movies gim on r.movieId = gim.movieId) " +
                    "INNER JOIN genres g on gim.genreId = g.id) " +
                "WHERE m.id = ? ";
        query_hidden =
                "SELECT id " +
                "FROM movies " +
                "WHERE id = ? AND hidden = 1 ";

        try {

            PreparedStatement ps_all = MovieService.getCon().prepareStatement(query_all);
            PreparedStatement ps_hidden = MovieService.getCon().prepareStatement(query_hidden);

            ps_all.setString(1, id);
            ps_all.setString(2, id);

            ps_hidden.setString(1, id);

            ServiceLogger.LOGGER.info("Trying query_all: " + ps_all.toString());
            ResultSet rs_all = ps_all.executeQuery();
            ServiceLogger.LOGGER.info("Query All succeeded.");

            ServiceLogger.LOGGER.info("Trying query_hidden: " + ps_hidden.toString());
            ResultSet rs_hidden = ps_hidden.executeQuery();
            ServiceLogger.LOGGER.info("Query Hidden succeeded.");

            MovieModel movieModel = new MovieModel();

            if (rs_hidden.next() && !allowHidden) {
                String hiddenMovie = rs_hidden.getString("id");
                if (hiddenMovie != null) {
                    ServiceLogger.LOGGER.warning("User tried to access hidden movie (" + hiddenMovie + ")");
                    return new MovieModel("HIDDEN");
                }
            } else {
                String movieId = "";
                String title = "";
                String director = "";
                Integer year = 0;
                String backdrop_path = "";
                Integer budget = 0;
                String overview = "";
                String poster_path = "";
                Integer revenue = 0;
                Float rating = Float.NaN;
                Integer numVotes = 0;
                GenreSimpleModel[] genres;
                StarSimpleModel[] stars;

                String[] starsId_array = new String[30];
                String[] starsName_array = new String[30];
                Integer[] starsYear_array = new Integer[30];
                int num_stars = 0;

                boolean results = false;

                // GETS THE MOVIE & STAR INFO
                if (rs_all.next()) {
                    movieId = rs_all.getString("id");
                    if (movieId == null)
                        return null;

                    title = rs_all.getString("title");
                    director = rs_all.getString("director");
                    year = rs_all.getInt("year");
                    backdrop_path = rs_all.getString("backdrop_path");
                    budget = rs_all.getInt("budget");
                    overview = rs_all.getString("overview");
                    poster_path = rs_all.getString("poster_path");
                    revenue = rs_all.getInt("revenue");
                    rating = rs_all.getFloat("rating");
                    numVotes = rs_all.getInt("numVotes");

                    starsId_array = buildArrayFromSQL(rs_all, "sg_id");
                    starsName_array = buildArrayFromSQL(rs_all, "sg_name");
                    starsYear_array = SQLData.integer(buildArrayFromSQL(rs_all, "s_year"));
                    num_stars = rs_all.getInt("cnt");
                }

                Integer[] genresId_array = new Integer[10];
                String[] genresName_array = new String[10];
                int num_genres = 0;

                // GETS GENRE INFO
                if (rs_all.next()) {
                    genresId_array = SQLData.integer(buildArrayFromSQL(rs_all, "sg_id"));
                    genresName_array = buildArrayFromSQL(rs_all, "sg_name");
                    num_genres = rs_all.getInt("cnt");

                    results = true;
                }


                ServiceLogger.LOGGER.info("Building " + num_stars + " stars.");
                // Convert Star ArrayList to Array
                ArrayList<StarSimple> stars_list = new ArrayList<StarSimple>();

                for (int i = 0; i < num_stars; i++) {
                    String star_id = starsId_array[i];
                    String name = starsName_array[i];
                    Integer birthYear = starsYear_array[i] == -1 ? null : starsYear_array[i];

                    StarSimple s = new StarSimple(star_id, name, birthYear);
                    stars_list.add(s);
                    ServiceLogger.LOGGER.info("Added " + s.getName() + "[" + i + "]");
                }

                stars = StarSimple.buildStarArray(stars_list);

                ServiceLogger.LOGGER.info("Building " + num_genres + " genres.");
                // Convert GenreDB ArrayList to Array
                ArrayList<GenreSimple> genres_list = new ArrayList<GenreSimple>();

                for (int i = 0; i < num_genres; i++) {
                    int genre_id = genresId_array[i];
                    String name = genresName_array[i];
                    GenreSimple g = new GenreSimple(genre_id, name);
                    genres_list.add(g);
                    ServiceLogger.LOGGER.info("Added " + g.getName());
                }

                genres = GenreSimple.buildGenreArray(genres_list);


                ServiceLogger.LOGGER.info("Building moviemodel.");
                if (results) {
                    ServiceLogger.LOGGER.info(title + " is being returned");
                    movieModel = new MovieModel(
                            movieId, title, director, year, backdrop_path, budget, overview,
                            poster_path, revenue, rating, numVotes, genres, stars);

                    return movieModel;
                } else {
                    ServiceLogger.LOGGER.info("No movie found.");
                }
            }

        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed.");
            e.printStackTrace();
        }

        return null;
    }

    public static int retrieveNumMovies(SearchMovieRequestModel requestModel) {
        String title = requestModel.getTitle();
        String genre = requestModel.getGenre();
        Integer year = requestModel.getYear();
        String director = requestModel.getDirector();

        ServiceLogger.LOGGER.info("Searching for how many movies were returned by this search...");

        String query;
        query =
                "SELECT COUNT(DISTINCT m.id) AS cnt " +
                        "FROM movies m " +
                        "LEFT OUTER JOIN ratings r on m.id = r.movieId " +
                        "LEFT OUTER JOIN genres_in_movies gim on m.id = gim.movieId " +
                        "LEFT OUTER JOIN genres g on gim.genreId = g.id " +
                        "WHERE ((? IS NULL) OR (m.title LIKE ?)) AND " +
                        "((? IS NULL) OR (g.name LIKE ?)) AND " +
                        "((? <= 0) OR (m.year = ?)) AND " +
                        "((? IS NULL) OR (m.director LIKE ?)) ";

        try {

            PreparedStatement ps = MovieService.getCon().prepareStatement(query);

            ps.setString(1, getSubstringText(title));
            ps.setString(2, getSubstringText(title));
            ps.setString(3, getSubstringText(genre));
            ps.setString(4, getSubstringText(genre));
            ps.setInt(5, year);
            ps.setInt(6, year);
            ps.setString(7, getSubstringText(director));
            ps.setString(8, getSubstringText(director));

            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");

           if (rs.next())
               return rs.getInt("cnt");

        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed.");
            e.printStackTrace();
        }

        return 0;
    }
}


