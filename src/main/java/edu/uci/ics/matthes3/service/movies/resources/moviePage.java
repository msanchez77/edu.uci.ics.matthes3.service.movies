package edu.uci.ics.matthes3.service.movies.resources;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.matthes3.service.movies.core.GetEndpoint;
import edu.uci.ics.matthes3.service.movies.core.UserRecords;
import edu.uci.ics.matthes3.service.movies.logger.ServiceLogger;
import edu.uci.ics.matthes3.service.movies.models.*;
import edu.uci.ics.matthes3.service.movies.utilities.DataValidator;
import edu.uci.ics.matthes3.service.movies.utilities.EndpointServices;
import edu.uci.ics.matthes3.service.movies.utilities.ResponseModelBuilder;
import edu.uci.ics.matthes3.service.movies.utilities.ResultCodes;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;

@Path("get")
public class moviePage {
    @GET
    @Path("{movieid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovieID(@Context HttpHeaders headers,
                               @PathParam("movieid") String movieid) {
        // Get the query parameters from the uri
        ServiceLogger.LOGGER.info("Receiving request to search for a movie ID...");

        // Get the email and sessionID from the HTTP header
        String[] h = EndpointServices.getHeaders(headers);
        String email = h[0];
        String sessionID = h[1];
        String transactionID = h[2];

        SearchMovieIDResponseModel responseModel;

        VerifyPrivilegeResponseModel privilegeResponseModel;
        boolean nonUserPrivilege = UserRecords.userPrivilegeSufficient(email, 4);

        MovieModel movieModel = UserRecords.retrieveMovieIDFromDB(movieid, nonUserPrivilege);
        ServiceLogger.LOGGER.info("Returned.");
        // No Movie found
        if (movieModel == null) {
            responseModel = ResponseModelBuilder.Get.constructResponseModel(211);
        }
        // User has insufficient privilege to see the movie they were searching for
        else if (movieModel.getMovieId().equals("HIDDEN")) {
            privilegeResponseModel = new VerifyPrivilegeResponseModel(
                    141,
                    ResultCodes.setMessage(141)
            );

            ServiceLogger.LOGGER.info(ResultCodes.setMessage(141));

            return Response.status(Status.OK).entity(privilegeResponseModel)
                    .header("email", email)
                    .header("sessionID", sessionID)
                    .header("transactionID", transactionID)
                    .build();
        }
        // Movie found
        else if (movieModel.getMovieId() != null) {
            responseModel = ResponseModelBuilder.Get.constructIDResponseModel(210, movieModel);
            ServiceLogger.LOGGER.info(responseModel.getMovie().getTitle() + " ready to be returned.");
        }
        // No Movie found
        else {
            responseModel = ResponseModelBuilder.Get.constructResponseModel(211);
        }

        return Response.status(Status.OK).entity(responseModel)
                .header("email", email)
                .header("sessionID", sessionID)
                .header("transactionID", transactionID)
                .build();

    }
}
