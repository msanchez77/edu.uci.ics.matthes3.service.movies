package edu.uci.ics.matthes3.service.movies.resources;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.matthes3.service.movies.core.MovieDbManagement;
import edu.uci.ics.matthes3.service.movies.exceptions.ModelValidationException;
import edu.uci.ics.matthes3.service.movies.logger.ServiceLogger;
import edu.uci.ics.matthes3.service.movies.models.*;
import edu.uci.ics.matthes3.service.movies.utilities.EndpointServices;
import edu.uci.ics.matthes3.service.movies.utilities.ModelValidator;
import edu.uci.ics.matthes3.service.movies.utilities.ResultCodes;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;

@Path("add")
public class addPage {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAddRequest(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("Getting request to add movie...");
        MovieAddRequestModel requestModel;
        MovieAddResponseModel responseModel;

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

        try {
            requestModel = (MovieAddRequestModel) ModelValidator.verifyModel(jsonText, MovieAddRequestModel.class);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, MovieAddRequestModel.class, email, sessionID, transactionID);
        }

        responseModel = MovieDbManagement.insertMovieIntoDb(requestModel);

        return Response.status(Status.OK).entity(responseModel)
                .header("email", email)
                .header("sessionID", sessionID)
                .header("transactionID", transactionID)
                .build();

    }

    private MovieAddResponseModel constructResponseModel(int resultCode, String movieId, int[] genreId) {
        MovieAddResponseModel responseModel;

        ServiceLogger.LOGGER.info("Constructing ID ResponseModel...");

        if (resultCode < -1 || resultCode == 215) {
            responseModel = new MovieAddResponseModel(
                    resultCode,
                    ResultCodes.setMessage(resultCode),
                    null,
                    null
            );
        } else /* resultCode == 214 or 216 */ {
            responseModel = new MovieAddResponseModel(
                    resultCode,
                    ResultCodes.setMessage(resultCode),
                    movieId,
                    genreId
            );
        }

        if (resultCode < -1)
            ServiceLogger.LOGGER.warning(ResultCodes.setMessage(resultCode));
        else if (resultCode > 0)
            ServiceLogger.LOGGER.info(ResultCodes.setMessage(resultCode));

        return responseModel;
    }
}
