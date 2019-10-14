package edu.uci.ics.matthes3.service.movies.core;

import edu.uci.ics.matthes3.service.movies.MovieService;
import edu.uci.ics.matthes3.service.movies.logger.ServiceLogger;
import edu.uci.ics.matthes3.service.movies.models.RatingAddRequestModel;
import edu.uci.ics.matthes3.service.movies.models.RatingAddResponseModel;
import edu.uci.ics.matthes3.service.movies.models.RemoveMovieResponseModel;
import edu.uci.ics.matthes3.service.movies.utilities.ResponseModelBuilder;
import edu.uci.ics.matthes3.service.movies.utilities.ResultCodes;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RatingDB {

    public static RatingAddResponseModel updateRatingFromDb(RatingAddRequestModel requestModel) {
        String movieId = requestModel.getId();
        Float userRating = requestModel.getRating();

        Float[] movieRatingInfo = getRating(movieId);
        if (movieRatingInfo == null) {
            // INSERT userrating and numvotes=1
            // IDK if this is will ever be caught
            ServiceLogger.LOGGER.info("No rating was found for movie " + movieId);
            return ResponseModelBuilder.Rating.constructAddResponseModel(251);
        }

        Float currentRating = movieRatingInfo[0];
        Integer currentVotes = Math.round(movieRatingInfo[1]);

        Float newRating = (currentVotes * currentRating + userRating) / (currentVotes + 1);
        ServiceLogger.LOGGER.info("New rating: " + newRating);
        newRating = roundFloat(newRating, 1);
        ServiceLogger.LOGGER.info("New rating (rounded): " + newRating);

        String update_query =
                "UPDATE ratings " +
                "SET rating = ?, numVotes = ? " +
                "WHERE movieId = ? ";

        try {
            PreparedStatement update_ps = MovieService.getCon().prepareStatement(update_query);

            update_ps.setFloat(1, newRating);
            update_ps.setInt(2, currentVotes+1);
            update_ps.setString(3, movieId);

            ServiceLogger.LOGGER.info("Trying query: " + update_ps.toString());
            update_ps.executeUpdate();
            ServiceLogger.LOGGER.info("Query succeeded.");

            return ResponseModelBuilder.Rating.constructAddResponseModel(250);
        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed.");
            e.printStackTrace();
        }

        return ResponseModelBuilder.Rating.constructAddResponseModel(251);

    }

    private static Float[] getRating(String movieId) {

        String query =
                "SELECT rating, numVotes " +
                "FROM ratings " +
                "WHERE movieId = ? ";

        try {
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ps.setString(1, movieId);

            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");

            if (rs.next()) {
                Float rating = rs.getFloat(1);
                Integer numVotes = rs.getInt(2);

                ServiceLogger.LOGGER.info("Movie: " + movieId);
                ServiceLogger.LOGGER.info("Rating: " + rating);
                ServiceLogger.LOGGER.info("NumVotes: " + numVotes);

                return new Float[]{rating, new Float(numVotes)};
            }

        } catch (SQLException e ) {
            ServiceLogger.LOGGER.warning("Query failed.");
            e.printStackTrace();
        }

        return null;
    }

    private static Float roundFloat(Float num, Integer decimal_place) {
        BigDecimal bd = new BigDecimal(num);
        bd = bd.setScale(decimal_place, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }
}
