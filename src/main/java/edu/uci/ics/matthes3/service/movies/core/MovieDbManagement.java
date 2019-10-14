package edu.uci.ics.matthes3.service.movies.core;

import edu.uci.ics.matthes3.service.movies.MovieService;
import edu.uci.ics.matthes3.service.movies.logger.ServiceLogger;
import edu.uci.ics.matthes3.service.movies.models.*;
import edu.uci.ics.matthes3.service.movies.utilities.ResultCodes;

import java.sql.*;

public class MovieDbManagement {
    private static final int NON_HIDDEN = 0;

    public static MovieAddResponseModel insertMovieIntoDb(MovieAddRequestModel requestModel) {
        String title = requestModel.getTitle();
        ServiceLogger.LOGGER.info("Title: " + title);
        String director = requestModel.getDirector();
        ServiceLogger.LOGGER.info("Director: " + director);
        Integer year = requestModel.getYear();
        ServiceLogger.LOGGER.info("Year: " + year);
        String backdrop_path = requestModel.getBackdrop_path();
        ServiceLogger.LOGGER.info("Backdrop: " + backdrop_path);
        Integer budget = requestModel.getBudget();
        ServiceLogger.LOGGER.info("Budget: " + budget);
        String overview = requestModel.getOverview();
        ServiceLogger.LOGGER.info("Overview: " + overview);
        String poster_path = requestModel.getPoster_path();
        ServiceLogger.LOGGER.info("Poster: " + poster_path);
        Integer revenue = requestModel.getRevenue();
        ServiceLogger.LOGGER.info("Revenue: " + revenue);
        GenreModel[] genres = requestModel.getGenres();
        ServiceLogger.LOGGER.info("Genres: " + genres);

        MovieAddResponseModel responseModel;

        if (isMovieAlreadyInDb(title)) {
            responseModel = new MovieAddResponseModel(
                    216,
                    ResultCodes.setMessage(216),
                    null,
                    null
            );
            ServiceLogger.LOGGER.info(ResultCodes.setMessage(216));
            return responseModel;
        }

        ServiceLogger.LOGGER.info("Adding movie...");
        String[] genreNames = extractGenreNames(genres);
        int[] genreIds = checkGenreInDb(genreNames);
        Integer nextMovieId = retrieveNextMovieId();
        String movieId = buildMovieId(nextMovieId);

        try {
            String insertMovie =
                    "INSERT INTO movies (id, title, year, director, backdrop_path, budget, overview, " +
                            "poster_path, revenue, hidden) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

            PreparedStatement insertPs = MovieService.getCon().prepareStatement(insertMovie);

            insertPs.setString(1, movieId);
            insertPs.setString(2, title);
            insertPs.setInt(3, year);
            insertPs.setString(4, director);

            if (backdrop_path == null)
                insertPs.setNull(5, Types.VARCHAR);
            else
                insertPs.setString(5, backdrop_path);

            if (budget == null)
                insertPs.setNull(6, Types.INTEGER);
            else
                insertPs.setInt(6, budget);

            if (overview == null)
                insertPs.setNull(7, Types.VARCHAR);
            else
                insertPs.setString(7, overview);

            if (poster_path == null)
                insertPs.setNull(8, Types.VARCHAR);
            else
                insertPs.setString(8, poster_path);

            if (revenue == null)
                insertPs.setNull(9, Types.INTEGER);
            else
                insertPs.setInt(9, revenue);

            insertPs.setInt(10, NON_HIDDEN);

            ServiceLogger.LOGGER.info("Trying query: " + insertPs.toString());
            int result = insertPs.executeUpdate();
            ServiceLogger.LOGGER.info("Query succeeded.");
            ServiceLogger.LOGGER.info("Result of execute update: " + result);

            if (result == 1) {
                insertGenresInMovies(movieId, genreIds);
                insertRatings(movieId);
//                int[] insertedMovieGenres = getMovieGenres(movieId, genres.length);

                responseModel = new MovieAddResponseModel(
                        214,
                        ResultCodes.setMessage(214),
                        movieId,
                        genreIds
                );
                ServiceLogger.LOGGER.info(ResultCodes.setMessage(214));
                return responseModel;
            }

        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed.");
            e.printStackTrace();
        }

        responseModel = new MovieAddResponseModel(
                215,
                ResultCodes.setMessage(215),
                null,
                null
        );
        ServiceLogger.LOGGER.info(ResultCodes.setMessage(215));
        return responseModel;
    }

    private static String[] extractGenreNames(GenreModel[] genres) {
        String[] names = new String[genres.length];

        for (int i = 0; i < genres.length; i++) {
            names[i] = genres[i].getName();
        }

        return names;
    }

    private static void insertRatings(String movieId) {
        String query =
                "INSERT INTO ratings (movieId, rating, numVotes) VALUES (?, ?, ?) ";

        try {
            PreparedStatement insertPs = MovieService.getCon().prepareStatement(query);

            insertPs.setString(1, movieId);
            insertPs.setFloat(2, 0.0f);
            insertPs.setInt(3, 0);

            ServiceLogger.LOGGER.info("Trying query: " + insertPs.toString());
            insertPs.executeUpdate();
            ServiceLogger.LOGGER.info("Query succeeded.");
        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed.");
            e.printStackTrace();
        }
    }

    private static void insertGenresInMovies(String movieId, int[] genreIds) {

        String query =
                "INSERT INTO genres_in_movies (genreId, movieId) VALUES (?, ?) ";

        try {
            PreparedStatement batchPs = MovieService.getCon().prepareStatement(query);

            for (int id : genreIds) {
                batchPs.setInt(1, id);
                batchPs.setString(2, movieId);
                batchPs.addBatch();
            }

            ServiceLogger.LOGGER.info("Trying query: " + batchPs.toString());
            int[] numUpdates = batchPs.executeBatch();
            ServiceLogger.LOGGER.info("Query succeeded: Added " + numUpdates.length + " genres to g_i_m.");
        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed.");
            e.printStackTrace();
        }
    }

    private static int[] getMovieGenres(String movieId, int length) {
        try {
            String query =
                    "SELECT gim.movieId, group_concat(gim.genreId) AS genres, COUNT(gim.genreId) AS cnt " +
                    "FROM genres_in_movies gim INNER JOIN movies m on gim.movieId = m.id " +
                    "WHERE gim.movieId = ? " +
                    "GROUP BY m.id ";

            PreparedStatement ps = MovieService.getCon().prepareStatement(query);

            ps.setString(1, movieId);

            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");

            String[] genresArrayStr;
            rs.next();
            if (rs.getInt("cnt") != 1)
                genresArrayStr = buildArrayFromSQL(rs, "genres");
            else
                genresArrayStr = new String[]{rs.getString("genres")};

            int[] genresArray = new int[length];

            for (int i = 0; i < length; i++) {
                int n = Integer.parseInt(genresArrayStr[i]);
                genresArray[i] = n;
            }

            return genresArray;

        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed.");
            e.printStackTrace();
        }

        return new int[0];
    }

    private static String buildMovieId(int newMovieId) {
        int id_length = String.valueOf(newMovieId).length();
        int padding = 10 - (2 + id_length);
        String zeros = new String(new char[padding]).replace("\0", "0");
        String movieId = "cs" + zeros + newMovieId;
        ServiceLogger.LOGGER.info("New MovieId: " + movieId);
        return movieId;
    }

    private static Boolean isMovieAlreadyInDb(String title) {
        try {
            String query =
                    "SELECT * " +
                    "FROM movies m " +
                    "WHERE m.title = ? " +
                    "GROUP BY m.id ";

            PreparedStatement ps = MovieService.getCon().prepareStatement(query);

            ps.setString(1, title);

            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");

            return rs.next();
        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed.");
            e.printStackTrace();
        }

        return true;
    }

    private static String[] buildArrayFromSQL(ResultSet rs, String column){
        try {
            String column_str = rs.getString(column);
            String[] column_arr = column_str.split(",");
            return column_arr;
        } catch (Exception e) {
            ServiceLogger.LOGGER.warning("Error building " + column + " array from SQL");
            return null;
        }
    }


    public static int[] checkGenreInDb(String[] genreNames) {
        try {
            String query =
                "SELECT * " +
                "FROM genres " +
                "WHERE name LIKE ? ";
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);

            int size = genreNames.length;

            int[] genreIds = new int[size];

            for (int i = 0; i < size; i++) {
                String name = genreNames[i];

                ps.setString(1, name);

                ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
                ResultSet rs = ps.executeQuery();
                ServiceLogger.LOGGER.info("Query succeeded.");


                int genreId;

                if (!rs.next()){
                    genreId = insertGenreInDb(name);
                    genreIds[i] = genreId;
                } else
                    genreIds[i] = rs.getInt("id");
            }

            return genreIds;
        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed.");
            e.printStackTrace();
        }

        return new int[0];
    }

    public static int insertGenreInDb(String name) {
        try {
            String insertGenre =
                    "INSERT INTO genres (name) VALUES (?); ";

            PreparedStatement insertPs = MovieService.getCon().prepareStatement(insertGenre, Statement.RETURN_GENERATED_KEYS);
            insertPs.setString(1, name);

            ServiceLogger.LOGGER.info("Trying query: " + insertPs.toString());
            insertPs.executeUpdate();
            ResultSet rs = insertPs.getGeneratedKeys();
            ServiceLogger.LOGGER.info("Query succeeded.");

            if (rs.next()) {
                ServiceLogger.LOGGER.info("INSERTED NEW GENRE");
                return Math.toIntExact(rs.getLong(1));
            }

        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed.");
            e.printStackTrace();
        }

        return 0;
    }

    public static int retrieveNextMovieId() {
        int nextMovieId;

        try {
            String query =
                "SELECT id " +
                "FROM movies " +
                "WHERE id LIKE 'cs%' " +
                "ORDER BY id DESC ";

        PreparedStatement ps = MovieService.getCon().prepareStatement(query);

        ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
        ResultSet rs = ps.executeQuery();
        ServiceLogger.LOGGER.info("Query succeeded.");

        if (rs.next()) {
            ServiceLogger.LOGGER.info("There are already custom movies in DB.");
            String lastMovieIdString = rs.getString("id");
            String sub = lastMovieIdString.substring(2);
            nextMovieId = Integer.parseInt(sub) + 1;
            ServiceLogger.LOGGER.info("Returning movieId: " + nextMovieId);
        } else
            nextMovieId = 1;

        return nextMovieId;

        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed.");
            e.printStackTrace();
        }
        return 0;
    }





    // ---------------------------------------------------------------------------------------------

    public static RemoveMovieResponseModel removeMovieFromDb(String id) {

        RemoveMovieResponseModel responseModel;

        ResultSet rs_check = isMovieIDAlreadyInDb(id);
        if (rs_check == null)
            ServiceLogger.LOGGER.info("isMovieIDAlreadyInDb returned NULL");

        try {
            if (!rs_check.next()) {
                responseModel = new RemoveMovieResponseModel(
                        241,
                        ResultCodes.setMessage(241)
                );
                return responseModel;
            }
            else if (rs_check.getInt(1) == 1) {
                responseModel = new RemoveMovieResponseModel(
                        242,
                        ResultCodes.setMessage(242)
                );
                return responseModel;
            }

            String updateQuery =
                    "UPDATE movies " +
                    "SET hidden = 1 " +
                    "WHERE movies.id = ? ";

            PreparedStatement updatePs = MovieService.getCon().prepareStatement(updateQuery);

            updatePs.setString(1, id);

            ServiceLogger.LOGGER.info("Trying query: " + updatePs.toString());
            updatePs.executeUpdate();
            ServiceLogger.LOGGER.info("Query succeeded.");

            responseModel = new RemoveMovieResponseModel(
                    240,
                    ResultCodes.setMessage(240)
            );
            ServiceLogger.LOGGER.info(ResultCodes.setMessage(240));
            return responseModel;

        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed.");
            e.printStackTrace();
        }

        return new RemoveMovieResponseModel(
                241,
                ResultCodes.setMessage(241)
        );
    }

    private static ResultSet isMovieIDAlreadyInDb(String id) {
        try {
            String query =
                    "SELECT hidden " +
                            "FROM movies m " +
                            "WHERE m.id = ? ";

            PreparedStatement ps = MovieService.getCon().prepareStatement(query);

            ps.setString(1, id);

            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");

            return rs;
        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed.");
            e.printStackTrace();
        }

        return null;
    }
}
