package edu.uci.ics.matthes3.service.movies.resources;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import edu.uci.ics.matthes3.service.movies.core.StarDB;
import edu.uci.ics.matthes3.service.movies.core.UserRecords;
import edu.uci.ics.matthes3.service.movies.exceptions.ModelValidationException;
import edu.uci.ics.matthes3.service.movies.logger.ServiceLogger;
import edu.uci.ics.matthes3.service.movies.models.Movie;
import edu.uci.ics.matthes3.service.movies.models.*;
import edu.uci.ics.matthes3.service.movies.utilities.*;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;
import javax.xml.ws.Service;
import java.io.IOException;
import java.util.ArrayList;

@Path("star")
public class starsPage {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStarRequest(
            @Context HttpHeaders headers,
            @QueryParam("name") String name,
            @QueryParam("birthYear") Integer birthYear,
            @QueryParam("movieTitle") String movieTitle,
            @QueryParam("limit") Integer limit,
            @QueryParam("offset") Integer offset,
            @DefaultValue("name") @QueryParam("orderby") String orderby,
            @DefaultValue("asc") @QueryParam("direction") String direction) {

        // Get the query parameters from the uri
        ServiceLogger.LOGGER.info("Receiving request to search for a star...");

//        ServiceLogger.LOGGER.info("id: " + id);
        ServiceLogger.LOGGER.info("name: " + name);
        ServiceLogger.LOGGER.info("birthYear: " + birthYear);
        ServiceLogger.LOGGER.info("movieTitle: " + movieTitle);
        ServiceLogger.LOGGER.info("limit: " + limit);
        ServiceLogger.LOGGER.info("offset: " + offset);
        ServiceLogger.LOGGER.info("orderBy: " + orderby);
        ServiceLogger.LOGGER.info("direction: " + direction);

        // Get the email and sessionID from the HTTP header
        String[] h = EndpointServices.getHeaders(headers);
        String email = h[0];
        String sessionID = h[1];
        String transactionID = h[2];

        SearchStarRequestModel requestModel;
        SearchStarResponseModel responseModel;

        if (birthYear == null || birthYear <= 1800)
            birthYear = 0;
        if (limit == null || limit < 0)
            limit = 10;
        if (offset == null || offset < 0)
            offset = 0;


        String jsonText = buildJsonText(name, birthYear, movieTitle, limit, offset, orderby, direction);
        ServiceLogger.LOGGER.info("JSON Text: " + jsonText);

        try {
            requestModel = (SearchStarRequestModel) ModelValidator.verifyModel(jsonText, SearchStarRequestModel.class);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, SearchStarRequestModel.class, email, sessionID, transactionID);
        }

        ArrayList<Star> stars_from_query = StarDB.retrieveStarsFromDB(requestModel);

        if (stars_from_query != null)
            responseModel = ResponseModelBuilder.Star.constructResponseModel(212, Star.buildStarArray(stars_from_query));
        else
            responseModel = ResponseModelBuilder.Star.constructResponseModel(213, null);

        return Response.status(Status.OK).entity(responseModel)
                .header("email", email)
                .header("sessionID", sessionID)
                .header("transactionID", transactionID)
                .build();

    }


    @Path("{id}")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStarID(@Context HttpHeaders headers,
                              @PathParam("id") String id) {
        // Get the query parameters from the uri
        ServiceLogger.LOGGER.info("Receiving request to search for a star ID...");

        // Get the email and sessionID from the HTTP header
        String[] h = EndpointServices.getHeaders(headers);
        String email = h[0];
        String sessionID = h[1];
        String transactionID = h[2];

        SearchStarIDResponseModel responseModel;
        StarModel starModel = StarDB.retrieveStarIDFromDB(id);

        if (starModel != null)
            responseModel = ResponseModelBuilder.Star.constructIDResponseModel(212, starModel);
        else
            responseModel = ResponseModelBuilder.Star.constructIDResponseModel(213, null);

        return Response.status(Status.OK).entity(responseModel)
                .header("email", email)
                .header("sessionID", sessionID)
                .header("transactionID", transactionID)
                .build();
    }


    @Path("add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addStar(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("Getting request to add a star to the database...");
        ObjectMapper mapper = new ObjectMapper();
        StarAddRequestModel requestModel;
        StarAddResponseModel responseModel;

        String[] h = EndpointServices.getHeaders(headers);
        String email = h[0];
        String sessionID = h[1];
        String transactionID = h[2];

        if (email == null){
            responseModel = new StarAddResponseModel(-16, ResultCodes.setMessage(-16));
            return Response.status(Status.BAD_REQUEST).entity(responseModel)
                    .header("email", email)
                    .header("sessionID", sessionID)
                    .header("transactionID", transactionID)
                    .build();
        }


        VerifyPrivilegeResponseModel privilegeResponseModel = EndpointServices.verifyPrivilege(email);
        if (privilegeResponseModel != null)
            return Response.status(Status.OK).entity(privilegeResponseModel)
                    .header("email", email)
                    .header("sessionID", sessionID)
                    .header("transactionID", transactionID)
                    .build();

        try {
            requestModel = (StarAddRequestModel) ModelValidator.verifyModel(jsonText, StarAddRequestModel.class);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, StarAddRequestModel.class, email, sessionID, transactionID);
        }

        responseModel = StarDB.addStarToDb(requestModel);

        if (responseModel == null) {
            ServiceLogger.LOGGER.info("ADD STAR RETURNED NULL");
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .header("email", email)
                    .header("sessionID", sessionID)
                    .header("transactionID", transactionID)
                    .build();
        } else {
            return Response.status(Status.OK).entity(responseModel)
                    .header("email", email)
                    .header("sessionID", sessionID)
                    .header("transactionID", transactionID)
                    .build();
        }
    }


    @Path("starsin")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addStarToMovie(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("Getting request to add a star to a movie...");
        StarAddToMovieRequestModel requestModel;
        StarAddResponseModel responseModel;

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
            requestModel = (StarAddToMovieRequestModel) ModelValidator.verifyModel(jsonText, StarAddToMovieRequestModel.class);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, StarAddToMovieRequestModel.class, email, sessionID, transactionID);
        }

        if (!StarDB.doesMovieExist(requestModel.getMovieid())) {
            responseModel = ResponseModelBuilder.Star.constructAddResponseModel(211);
            return Response.status(Status.OK).entity(responseModel)
                    .header("email", email)
                    .header("sessionID", sessionID)
                    .header("transactionID", transactionID)
                    .build();
        }

        responseModel = StarDB.addStarToMovie(requestModel.getStarid(), requestModel.getMovieid());
        return Response.status(Status.OK).entity(responseModel)
                .header("email", email)
                .header("sessionID", sessionID)
                .header("transactionID", transactionID)
                .build();
    }


    private String buildJsonText(String name, Integer birthYear, String movieTitle, Integer limit, Integer offset, String orderby, String direction) {
        String jsonText = "{";
        if (name != null)
            jsonText += String.format("\"name\":\"%s\"", name);
        if (birthYear != null)
            jsonText += String.format(",\"birthYear\":%d", birthYear);
        if (movieTitle != null)
            jsonText += String.format(",\"movieTitle\":\"%s\"", movieTitle);

        jsonText += String.format(",\"offset\":%d", offset);
        jsonText += String.format(",\"limit\":%d", limit);
        jsonText += String.format(",\"orderby\":\"%s\"", orderby);
        jsonText += String.format(",\"direction\":\"%s\"", direction);
        jsonText += "}";

        ServiceLogger.LOGGER.info("buildJsonText successfully ran.");
        return jsonText;
    }
}
