package edu.uci.ics.matthes3.service.movies.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.matthes3.service.movies.core.GenreDB;
import edu.uci.ics.matthes3.service.movies.exceptions.ModelValidationException;
import edu.uci.ics.matthes3.service.movies.logger.ServiceLogger;
import edu.uci.ics.matthes3.service.movies.models.*;
import edu.uci.ics.matthes3.service.movies.utilities.EndpointServices;
import edu.uci.ics.matthes3.service.movies.utilities.ModelValidator;
import edu.uci.ics.matthes3.service.movies.utilities.ResponseModelBuilder;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;

@Path("genre")
public class genrePage {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGenre(@Context HttpHeaders headers) {
        ServiceLogger.LOGGER.info("Getting request to get all the genres in the database...");
        ObjectMapper mapper = new ObjectMapper();
        GetGenreResponseModel responseModel;

        String[] h = EndpointServices.getHeaders(headers);
        String email = h[0];
        String sessionID = h[1];
        String transactionID = h[2];

//        VerifyPrivilegeResponseModel privilegeResponseModel = EndpointServices.verifyPrivilege(email);
//        if (privilegeResponseModel != null)
//            return Response.status(Status.OK).entity(privilegeResponseModel)
//                    .header("email", email)
//                    .header("sessionID", sessionID)
//                    .header("transactionID", transactionID)
//                    .build();

        GenreSimpleModel[] genres = GenreDB.getGenresFromDb();

        responseModel = ResponseModelBuilder.Genre.constructGenreResponseModel(219, genres);

        return Response.status(Status.OK).entity(responseModel)
                .header("email", email)
                .header("sessionID", sessionID)
                .header("transactionID", transactionID)
                .build();
    }


    @POST
    @Path("add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addGenre(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("Getting request to add a genre to the database...");
        GenreAddRequestModel requestModel;
        GetGenreResponseModel responseModel;

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
            requestModel = (GenreAddRequestModel) ModelValidator.verifyModel(jsonText, GenreAddRequestModel.class);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, GenreAddRequestModel.class, email, sessionID, transactionID);
        }

        int resultCode = GenreDB.addGenreToDb(requestModel.getName());
        responseModel = ResponseModelBuilder.Genre.constructResponseModel(resultCode);

        return Response.status(Status.OK).entity(responseModel)
                .header("email", email)
                .header("sessionID", sessionID)
                .header("transactionID", transactionID)
                .build();
    }


    @GET
    @Path("{movieid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGenreFromMovieId(@Context HttpHeaders headers,
                                        @PathParam("movieid") String movieid) {
        ServiceLogger.LOGGER.info("Getting request to get all the genres for a particular movie...");
        ObjectMapper mapper = new ObjectMapper();
        GetGenreResponseModel responseModel;

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

        GenreSimpleModel[] genresInMovie = GenreDB.getAllGenresForMovie(movieid);

        if (genresInMovie == null)
            responseModel = ResponseModelBuilder.Genre.constructGenreResponseModel(211, null);
        else
            responseModel = ResponseModelBuilder.Genre.constructGenreResponseModel(219, genresInMovie);

        return Response.status(Status.OK).entity(responseModel)
                .header("email", email)
                .header("sessionID", sessionID)
                .header("transactionID", transactionID)
                .build();
    }

}
