package edu.uci.ics.matthes3.service.movies.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.matthes3.service.movies.core.GenreDB;
import edu.uci.ics.matthes3.service.movies.core.RatingDB;
import edu.uci.ics.matthes3.service.movies.core.StarDB;
import edu.uci.ics.matthes3.service.movies.exceptions.ModelValidationException;
import edu.uci.ics.matthes3.service.movies.logger.ServiceLogger;
import edu.uci.ics.matthes3.service.movies.models.*;
import edu.uci.ics.matthes3.service.movies.utilities.EndpointServices;
import edu.uci.ics.matthes3.service.movies.utilities.ModelValidator;
import edu.uci.ics.matthes3.service.movies.utilities.ResponseModelBuilder;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;

@Path("rating")
public class ratingPage {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addRating(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("Getting request to add a genre to the database...");
        RatingAddRequestModel requestModel;
        RatingAddResponseModel responseModel;

        String[] h = EndpointServices.getHeaders(headers);
        String email = h[0];
        String sessionID = h[1];
        String transactionID = h[2];

        try {
            requestModel = (RatingAddRequestModel) ModelValidator.verifyModel(jsonText, RatingAddRequestModel.class);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, RatingAddRequestModel.class, email, sessionID, transactionID);
        }

        if (!StarDB.doesMovieExist(requestModel.getId())) {
            responseModel = ResponseModelBuilder.Rating.constructAddResponseModel(211);
            return Response.status(Status.OK).entity(responseModel)
                    .header("email", email)
                    .header("sessionID", sessionID)
                    .header("transactionID", transactionID)
                    .build();
        }

        responseModel = RatingDB.updateRatingFromDb(requestModel);

        return Response.status(Status.OK).entity(responseModel)
                .header("email", email)
                .header("sessionID", sessionID)
                .header("transactionID", transactionID)
                .build();
    }

}
