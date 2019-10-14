package edu.uci.ics.matthes3.service.movies.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.matthes3.service.movies.core.MovieDbManagement;
import edu.uci.ics.matthes3.service.movies.core.UserRecords;
import edu.uci.ics.matthes3.service.movies.logger.ServiceLogger;
import edu.uci.ics.matthes3.service.movies.models.*;
import edu.uci.ics.matthes3.service.movies.utilities.EndpointServices;
import edu.uci.ics.matthes3.service.movies.utilities.ResultCodes;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;

@Path("delete")
public class deletePage {
    @DELETE
    @Path("{movieid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRemoveRequest(@Context HttpHeaders headers,
                                     @PathParam("movieid") String movieid) {
        ServiceLogger.LOGGER.info("Getting request to remove movie...");
        RemoveMovieResponseModel responseModel;

        String[] h = EndpointServices.getHeaders(headers);
        String email = h[0];
        String sessionID = h[1];
        String transactionID = h[2];

        VerifyPrivilegeResponseModel privilegeResponseModel = EndpointServices.verifyPrivilege(email);
        if (privilegeResponseModel != null)
            return Response.status(Status.OK).entity(privilegeResponseModel)
                    .header("email", email)
                    .header("sessionID", sessionID)
                    .header("transactionID", transactionID)
                    .build();

        responseModel = MovieDbManagement.removeMovieFromDb(movieid);

        return Response.status(Status.OK).entity(responseModel)
                .header("email", email)
                .header("sessionID", sessionID)
                .header("transactionID", transactionID)
                .build();
    }
}