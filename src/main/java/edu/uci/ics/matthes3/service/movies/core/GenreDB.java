package edu.uci.ics.matthes3.service.movies.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.matthes3.service.movies.MovieService;
import edu.uci.ics.matthes3.service.movies.logger.ServiceLogger;
import edu.uci.ics.matthes3.service.movies.models.GenreSimpleModel;

import javax.xml.ws.Service;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static edu.uci.ics.matthes3.service.movies.core.MovieDbManagement.insertGenreInDb;

public class GenreDB {
    public static GenreSimpleModel[] getGenresFromDb() {
        ObjectMapper mapper = new ObjectMapper();

        String query =
                "SELECT GROUP_CONCAT(CONCAT('{\"id\": ', id, ', \"name\": \"', name, '\"}') SEPARATOR '/') " +
                "FROM genres ";

        try {
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);

            ResultSet rs = ps.executeQuery();


            if (rs.next()) {
                String s = rs.getString(1);
                String[] split = s.split("/");

                GenreSimpleModel[] genres = new GenreSimpleModel[split.length];
                for (int i = 0; i < split.length; i++) {
                    GenreSimpleModel g = mapper.readValue(split[i], GenreSimpleModel.class);
                    genres[i] = g;
                }

                return genres;
            }

        } catch (SQLException | IOException e) {
            ServiceLogger.LOGGER.warning("Query failed.");
            e.printStackTrace();
        }

        return null;
    }

    public static int addGenreToDb(String name) {
        try {
            String query =
                    "SELECT * " +
                    "FROM genres " +
                    "WHERE name LIKE ? ";
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);

            ps.setString(1, name);

            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");

            if (!rs.next()) {
                insertGenreInDb(name);
                return 217;
            }

        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed.");
            e.printStackTrace();
        }

        return 218;
    }


    public static GenreSimpleModel[] getAllGenresForMovie(String movieid) {
        ObjectMapper mapper = new ObjectMapper();

        String query =
                "SELECT GROUP_CONCAT(CONCAT('{\"id\": ', id, ', \"name\": \"', name, '\"}') SEPARATOR '/') " +
                "FROM genres INNER JOIN genres_in_movies gim on genres.id = gim.genreId " +
                "WHERE movieId = ? ";

        try {
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ps.setString(1, movieid);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String s = rs.getString(1);

                if (s == null)
                    return null;

                String[] split = s.split("/");

                GenreSimpleModel[] genres = new GenreSimpleModel[split.length];
                for (int i = 0; i < split.length; i++) {
                    GenreSimpleModel g = mapper.readValue(split[i], GenreSimpleModel.class);
                    genres[i] = g;
                }

                return genres;
            }

        } catch (SQLException | IOException e) {
            ServiceLogger.LOGGER.warning("Query failed.");
            e.printStackTrace();
        }

        return null;
    }
}
