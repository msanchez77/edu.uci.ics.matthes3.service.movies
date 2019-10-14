package edu.uci.ics.matthes3.service.movies.core;

import edu.uci.ics.matthes3.service.movies.MovieService;
import edu.uci.ics.matthes3.service.movies.logger.ServiceLogger;
import edu.uci.ics.matthes3.service.movies.models.*;
import edu.uci.ics.matthes3.service.movies.utilities.ResponseModelBuilder;
import edu.uci.ics.matthes3.service.movies.utilities.ResultCodes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import static edu.uci.ics.matthes3.service.movies.utilities.SQLData.getSubstringText;

public class StarDB {

    public static ArrayList<Star> retrieveStarsFromDB(SearchStarRequestModel requestModel) {
        String name = requestModel.getName();
        int birthYear = requestModel.getBirthYear();
        String movieTitle = requestModel.getMovieTitle();
        int limit = requestModel.getLimit();
        int offset = requestModel.getOffset();
        String orderBy = requestModel.getOrderBy();
        String direction = requestModel.getDirection();

        ServiceLogger.LOGGER.info("Searching for star...");

        String query =
                "SELECT s.id, name, IFNULL(birthYear, -1) AS birthYear " +
                        "FROM ((stars AS s " +
                        "LEFT OUTER JOIN stars_in_movies sim on s.id = sim.starId) " +
                        "LEFT OUTER JOIN movies m on sim.movieId = m.id) " +
                        "WHERE ((? IS NULL) OR (s.name LIKE ?)) AND " +
                        "((? <= 0) OR (s.birthYear = ?)) AND " +
                        "((? IS NULL) OR (m.title LIKE ?)) " +
                        "ORDER BY %s %s, %s %s " +
                        "LIMIT ? " +
                        "OFFSET ? ";
        try {
            String secondary_orderby = orderBy.equals("name") ? "s.birthyear" : "s.name";
            String secondary_direction = "asc";
            query = String.format(query, orderBy, direction, secondary_orderby, secondary_direction);

            PreparedStatement ps = MovieService.getCon().prepareStatement(query);

            ps.setString(1, getSubstringText(name));
            ps.setString(2, getSubstringText(name));
            ps.setInt(3, birthYear);
            ps.setInt(4, birthYear);
            ps.setString(5, getSubstringText(movieTitle));
            ps.setString(6, getSubstringText(movieTitle));
            ps.setInt(7, limit);
            ps.setInt(8, offset);

            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");

            ArrayList<Star> stars = new ArrayList<>();
            int num_stars = 0;
            while (rs.next()) {
                String currentId = rs.getString("id");
                ServiceLogger.LOGGER.info("Retrieved star id: " + currentId);

                Star s = new Star(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getInt("birthYear") == -1 ? null : rs.getInt("birthYear")
                );

                stars.add(s);
                num_stars++;
            }

            ServiceLogger.LOGGER.info("Retrieved " + num_stars + " stars.");
            ServiceLogger.LOGGER.info("Exiting retrieveStarsFromDB...");
            if (stars.size() == 0)
                return null;

            return stars;

        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed.");
            e.printStackTrace();
        }

        return null;
    }

    public static StarModel retrieveStarIDFromDB(String id) {

        ServiceLogger.LOGGER.info("Searching for a star ID...");

        String query =
                "SELECT id, name, IFNULL(birthYear, -1) AS birthYear " +
                        "FROM stars AS s " +
                        "WHERE s.id = ? ";

        try {

            PreparedStatement ps = MovieService.getCon().prepareStatement(query);

            ps.setString(1, id);

            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");

            StarModel starModel = new StarModel();

            if (rs.next()) {
                String currentName = rs.getString("name");
                ServiceLogger.LOGGER.info("Retrieved star: " + currentName);

                starModel = new StarModel(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getInt("birthYear") == -1 ? null : rs.getInt("birthYear")
                );

                return starModel;
            }

        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed.");
            e.printStackTrace();
        }

        return null;
    }

    public static StarAddResponseModel addStarToDb(StarAddRequestModel requestModel) {

        String name = requestModel.getName();
        Integer birthYear = requestModel.getBirthYear();
        if (birthYear != null && birthYear > 2019) {
            ServiceLogger.LOGGER.info("Received invalid birthYear. Defaulting to null.");
            birthYear = null;
        }

        ServiceLogger.LOGGER.info("Name: " + name);
        ServiceLogger.LOGGER.info("Birth Year: " + birthYear);

        StarAddResponseModel responseModel;

        if (isStarAlreadyInDb(name, birthYear)) {
            responseModel = new StarAddResponseModel(
                    222,
                    ResultCodes.setMessage(222)
            );

            return responseModel;
        }

        ServiceLogger.LOGGER.info("Adding star...");
        Integer nextStarId = retrieveNextStarId();
        String starId = buildStarId(nextStarId);

        try {
            String insertStar =
                    "INSERT INTO stars (id, name, birthYear) VALUES (?, ?, ?);";

            PreparedStatement insertPs = MovieService.getCon().prepareStatement(insertStar);

            insertPs.setString(1, starId);
            insertPs.setString(2, name);
            if (birthYear == null)
                insertPs.setNull(3, Types.INTEGER);
            else
                insertPs.setInt(3, birthYear);

            ServiceLogger.LOGGER.info("Trying query: " + insertPs.toString());
            int result = insertPs.executeUpdate();
            ServiceLogger.LOGGER.info("Query succeeded.");

            if (result == 1) {
                responseModel = new StarAddResponseModel(
                        220,
                        ResultCodes.setMessage(220)
                );
                ServiceLogger.LOGGER.info(ResultCodes.setMessage(220));
                return responseModel;
            } else {
                responseModel = new StarAddResponseModel(
                        221,
                        ResultCodes.setMessage(221)
                );
                ServiceLogger.LOGGER.info(ResultCodes.setMessage(215));
                return responseModel;
            }

        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed.");
            e.printStackTrace();
        }

        return null;
    }

    private static boolean isStarAlreadyInDb(String name, Integer birthYear) {
        try {
            String query =
                    "SELECT COUNT(*) " +
                    "FROM stars s " +
                    "WHERE s.name = ? AND ";

            PreparedStatement ps;
            if (birthYear == null) {
                query += "s.birthYear IS NULL ";
                ps = MovieService.getCon().prepareStatement(query);
                ps.setString(1, name);
            } else {
                query += "s.birthYear = ? ";
                ps = MovieService.getCon().prepareStatement(query);
                ps.setString(1, name);
                ps.setInt(2, birthYear);
            }

            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");

            if (rs.next())
                return rs.getInt(1) == 1;

            ServiceLogger.LOGGER.info("111111111111");
        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed.");
            e.printStackTrace();
        }

        return false;
    }

    public static int retrieveNextStarId() {
        int nextStarId;

        try {
            String query =
                    "SELECT id " +
                    "FROM stars " +
                    "WHERE id LIKE 'ss%' " +
                    "ORDER BY id DESC ";

            PreparedStatement ps = MovieService.getCon().prepareStatement(query);

            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");

            if (rs.next()) {
                ServiceLogger.LOGGER.info("There are already custom stars in DB.");
                String lastStarIdString = rs.getString("id");
                String sub = lastStarIdString.substring(2);
                nextStarId = Integer.parseInt(sub) + 1;
                ServiceLogger.LOGGER.info("Returning movieId: " + nextStarId);
            } else
                nextStarId = 1;

            return nextStarId;

        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed.");
            e.printStackTrace();
        }
        return 0;
    }

    private static String buildStarId(Integer newStarId) {
        int id_length = String.valueOf(newStarId).length();
        int padding = 10 - (2 + id_length);
        String zeros = new String(new char[padding]).replace("\0", "0");
        String starId = "ss" + zeros + newStarId;
        ServiceLogger.LOGGER.info("New StarId: " + starId);
        return starId;
    }

    public static boolean doesMovieExist(String movieid) {
        try {
            String query =
                    "SELECT COUNT(*) " +
                    "FROM movies m " +
                    "WHERE m.id = ? ";

            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ps.setString(1, movieid);

            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");

            if (rs.next())
                return rs.getInt(1) == 1;

            ServiceLogger.LOGGER.info("111111111111");
        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed.");
            e.printStackTrace();
        }

        return false;
    }

    public static StarAddResponseModel addStarToMovie(String starid, String movieid) {

        String insert_query =
                "INSERT INTO stars_in_movies VALUES (?, ?);";

        try {
            PreparedStatement ps = MovieService.getCon().prepareStatement(insert_query);
            ps.setString(1, starid);
            ps.setString(2, movieid);

            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            int num_updates = ps.executeUpdate();
            ServiceLogger.LOGGER.info("Query succeeded: " + num_updates);

            if (num_updates == 1) {
                return ResponseModelBuilder.Star.constructAddResponseModel(230);
            } else {
                return ResponseModelBuilder.Star.constructAddResponseModel(232);
            }

        } catch (SQLException e ) {
            ServiceLogger.LOGGER.warning("Query failed.");
            e.printStackTrace();
        }

        return ResponseModelBuilder.Star.constructAddResponseModel(231);
    }
}
