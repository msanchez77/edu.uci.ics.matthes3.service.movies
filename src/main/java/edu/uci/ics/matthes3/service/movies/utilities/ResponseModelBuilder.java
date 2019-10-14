package edu.uci.ics.matthes3.service.movies.utilities;

import edu.uci.ics.matthes3.service.movies.logger.ServiceLogger;
import edu.uci.ics.matthes3.service.movies.models.*;

public class ResponseModelBuilder {

    public static class Search {
        public static SearchMovieResponseModel constructSearchResponseModel(int resultCode, MovieSearchModel[] movies, int numResults) {
            SearchMovieResponseModel responseModel;

            responseModel = new SearchMovieResponseModel(
                    resultCode,
                    setMessage(resultCode),
                    movies,
                    numResults
            );

            if (resultCode < -1)
                ServiceLogger.LOGGER.warning(setMessage(resultCode));
            else if (resultCode > 0)
                ServiceLogger.LOGGER.info(setMessage(resultCode));

            return responseModel;
        }

        public static SearchMovieResponseModel constructResponseModel(int resultCode) {
            SearchMovieResponseModel responseModel;

            responseModel = new SearchMovieResponseModel(
                    resultCode,
                    setMessage(resultCode)
            );

            if (resultCode < -1)
                ServiceLogger.LOGGER.warning(setMessage(resultCode));
            else if (resultCode > 0)
                ServiceLogger.LOGGER.info(setMessage(resultCode));

            return responseModel;
        }
    }

    public static class Get {
        public static SearchMovieIDResponseModel constructResponseModel(int resultCode) {
            SearchMovieIDResponseModel responseModel;

            ServiceLogger.LOGGER.info("Constructing ID ResponseModel...");

            responseModel = new SearchMovieIDResponseModel(
                    resultCode,
                    setMessage(resultCode)
            );

            if (resultCode < -1)
                ServiceLogger.LOGGER.warning(setMessage(resultCode));
            else if (resultCode > 0)
                ServiceLogger.LOGGER.info(setMessage(resultCode));

            return responseModel;
        }

        public static SearchMovieIDResponseModel constructIDResponseModel(int resultCode, MovieModel movieModel) {
            SearchMovieIDResponseModel responseModel;

            ServiceLogger.LOGGER.info("Constructing ID ResponseModel...");

            ServiceLogger.LOGGER.info("Building " + movieModel.getTitle() + " from ID.");
            responseModel = new SearchMovieIDResponseModel(
                    resultCode,
                    setMessage(resultCode),
                    movieModel
            );

            if (resultCode < -1)
                ServiceLogger.LOGGER.warning(setMessage(resultCode));
            else if (resultCode > 0)
                ServiceLogger.LOGGER.info(setMessage(resultCode));

            return responseModel;
        }
    }

    public static class Genre {
        public static GetGenreResponseModel constructResponseModel(int resultCode) {
            GetGenreResponseModel responseModel;

            ServiceLogger.LOGGER.info("Constructing ID ResponseModel...");

            responseModel = new GetGenreResponseModel(
                    resultCode,
                    setMessage(resultCode)
            );

            if (resultCode < -1)
                ServiceLogger.LOGGER.warning(setMessage(resultCode));
            else if (resultCode > 0)
                ServiceLogger.LOGGER.info(setMessage(resultCode));

            return responseModel;
        }

        public static GetGenreResponseModel constructGenreResponseModel(int resultCode, GenreSimpleModel[] genres) {
            GetGenreResponseModel responseModel;

            ServiceLogger.LOGGER.info("Constructing ID ResponseModel...");

            responseModel = new GetGenreResponseModel(
                    resultCode,
                    setMessage(resultCode),
                    genres
            );

            if (resultCode < -1)
                ServiceLogger.LOGGER.warning(setMessage(resultCode));
            else if (resultCode > 0)
                ServiceLogger.LOGGER.info(setMessage(resultCode));

            return responseModel;
        }
    }

    public static class Star {
        public static SearchStarResponseModel constructResponseModel(int resultCode, StarModel[] stars) {
            SearchStarResponseModel responseModel;

            ServiceLogger.LOGGER.info("Constructing StarResponseModel...");

            if (resultCode != 212) {
                responseModel = new SearchStarResponseModel(
                        resultCode,
                        setMessage(resultCode),
                        null
                );
            } else {
                responseModel = new SearchStarResponseModel(
                        resultCode,
                        setMessage(resultCode),
                        stars
                );
            }

            if (resultCode < -1)
                ServiceLogger.LOGGER.warning(setMessage(resultCode));
            else if (resultCode > 0)
                ServiceLogger.LOGGER.info(setMessage(resultCode));

            return responseModel;
        }

        public static SearchStarIDResponseModel constructIDResponseModel(int resultCode, StarModel star) {
            SearchStarIDResponseModel responseModel;

            ServiceLogger.LOGGER.info("Constructing StarID ResponseModel...");

            if (resultCode != 212) {
                responseModel = new SearchStarIDResponseModel(
                        resultCode,
                        setMessage(resultCode),
                        null
                );
            } else {
                responseModel = new SearchStarIDResponseModel(
                        resultCode,
                        setMessage(resultCode),
                        star
                );
            }

            if (resultCode < -1)
                ServiceLogger.LOGGER.warning(setMessage(resultCode));
            else if (resultCode > 0)
                ServiceLogger.LOGGER.info(setMessage(resultCode));

            return responseModel;
        }

        public static StarAddResponseModel constructAddResponseModel(int resultCode) {
            StarAddResponseModel responseModel;

            ServiceLogger.LOGGER.info("Constructing Star Add to Movie ResponseModel...");

            responseModel = new StarAddResponseModel(
                    resultCode,
                    setMessage(resultCode)
            );

            if (resultCode < -1)
                ServiceLogger.LOGGER.warning(setMessage(resultCode));
            else if (resultCode > 0)
                ServiceLogger.LOGGER.info(setMessage(resultCode));

            return responseModel;
        }
    }

    public static class Rating {

        public static RatingAddResponseModel constructAddResponseModel(int resultCode) {
            RatingAddResponseModel responseModel;

            ServiceLogger.LOGGER.info("Constructing Star Add to Movie ResponseModel...");

            responseModel = new RatingAddResponseModel(
                    resultCode,
                    setMessage(resultCode)
            );

            if (resultCode < -1)
                ServiceLogger.LOGGER.warning(setMessage(resultCode));
            else if (resultCode > 0)
                ServiceLogger.LOGGER.info(setMessage(resultCode));

            return responseModel;
        }

    }


    private static String setMessage(int resultCode) {
        if (resultCode == -17)
            return "SessionID not provided in request header.";
        if (resultCode == -16)
            return "Email not provided in request header.";
        if (resultCode == -15)
            return "User ID number is out of valid range.";
        if (resultCode == -14)
            return "Privilege level out of valid range.";
        if (resultCode == -13)
            return "Token has invalid length.";
        if (resultCode == -12)
            return "Password has invalid length.";
        if (resultCode == -11)
            return "Email has invalid format.";
        if (resultCode == -10)
            return "Email has invalid length.";
        if (resultCode == -3)
            return "JSON parse Exception.";
        if (resultCode == -2)
            return "JSON mapping Exception.";
        if (resultCode == -1)
            return "Internal server error.";
        if (resultCode == 11)
            return "Passwords do not match.";
        if (resultCode == 12)
            return "Password does not meet length requirements.";
        if (resultCode == 13)
            return "Password does not meet character requirements.";
        if (resultCode == 14)
            return "User not found.";
        if (resultCode == 16)
            return "Email already in use.";
        if (resultCode == 110)
            return "User registered successfully.";
        if (resultCode == 120)
            return "User logged in successfully.";
        if (resultCode == 130)
            return "Session is active.";
        if (resultCode == 131)
            return "Session is expired.";
        if (resultCode == 132)
            return "Session is closed.";
        if (resultCode == 133)
            return "Session is revoked.";
        if (resultCode == 140)
            return "User has sufficient privilege level.";
        if(resultCode == 141)
            return "User has insufficient privilege level.";
        if(resultCode == 150)
            return "Password updated successfully.";
        if(resultCode == 160)
            return "User successfully retrieved.";
        if(resultCode == 170)
            return "User Created.";
        if(resultCode == 171)
            return "Creating user with \"ROOT\" privilege is not allowed.";
        if(resultCode == 180)
            return "User updated.";
        if(resultCode == 181)
            return "Users cannot be elevated to root privilege level.";
        if(resultCode == 210)
            return "Found movies with search parameters.";
        if(resultCode == 211)
            return "No movies found with search parameters.";
        if(resultCode == 212)
            return "Found stars with search parameters.";
        if(resultCode == 213)
            return "No stars found with search parameters.";
        if(resultCode == 214)
            return "Movie successfully added.";
        if(resultCode == 215)
            return "Could not add movie.";
        if(resultCode == 216)
            return "Movie already exists.";
        if(resultCode == 217)
            return "GenreDB successfully added.";
        if(resultCode == 218)
            return "GenreDB could not be added.";
        if (resultCode == 219)
            return "Genres successfully retrieved.";
        if (resultCode == 220)
            return "Star successfully added.";
        if (resultCode == 221)
            return "Could not add star.";
        if (resultCode == 222)
            return "Star already exists.";
        if (resultCode == 230)
            return "Star successfully added to movie.";
        if (resultCode == 231)
            return "Could not add star to movie.";
        if (resultCode == 232)
            return "Star already exists in movie.";
        if (resultCode == 240)
            return "Movie successfully removed.";
        if (resultCode == 241)
            return "Could not remove movie.";
        if (resultCode == 242)
            return "Movie has been already removed.";
        if (resultCode == 250)
            return "Rating successfully updated.";
        if (resultCode == 251)
            return "Could not update rating.";
        return "INVALID RESULT CODE";
    }
}
