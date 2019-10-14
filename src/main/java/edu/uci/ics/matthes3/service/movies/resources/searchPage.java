package edu.uci.ics.matthes3.service.movies.resources;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.matthes3.service.movies.core.UserRecords;
import edu.uci.ics.matthes3.service.movies.exceptions.ModelValidationException;
import edu.uci.ics.matthes3.service.movies.logger.ServiceLogger;
import edu.uci.ics.matthes3.service.movies.models.*;
import edu.uci.ics.matthes3.service.movies.utilities.EndpointServices;
import edu.uci.ics.matthes3.service.movies.utilities.ModelValidator;
import edu.uci.ics.matthes3.service.movies.utilities.ResponseModelBuilder;
import edu.uci.ics.matthes3.service.movies.utilities.ResultCodes;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;

@Path("search")
public class searchPage {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSearchRequest(
            @Context HttpHeaders headers,
            @QueryParam("title") String title,
            @QueryParam("genre") String genre,
            @QueryParam("year") Integer year,
            @QueryParam("director") String director,
            @QueryParam("hidden") Boolean hidden,
            @QueryParam("limit") Integer limit,
            @QueryParam("offset") Integer offset,
            @DefaultValue("rating") @QueryParam("orderby") String orderby,
            @DefaultValue("desc") @QueryParam("direction") String direction) {

        // Get the query parameters from the uri
        ServiceLogger.LOGGER.info("Receiving request to search for a movie...");

//        ServiceLogger.LOGGER.info("id: " + id);
        ServiceLogger.LOGGER.info("title: " + title);
        ServiceLogger.LOGGER.info("genre: " + genre);
        ServiceLogger.LOGGER.info("year: " + year);
        ServiceLogger.LOGGER.info("director: " + director);
        ServiceLogger.LOGGER.info("hidden: " + hidden);
        ServiceLogger.LOGGER.info("limit: " + limit);
        ServiceLogger.LOGGER.info("offset: " + offset);
        ServiceLogger.LOGGER.info("orderby: " + orderby);
        ServiceLogger.LOGGER.info("direction: " + direction);

        // Get the email and sessionID from the HTTP header
        String[] h = EndpointServices.getHeaders(headers);
        String email = h[0];
        String sessionID = h[1];
        String transactionID = h[2];

        SearchMovieRequestModel requestModel;
        SearchMovieResponseModel responseModel;

        if (year == null || year < 0)
            year = 0;
        if (offset == null || offset < 0)
            offset = 0;
        if (limit == null || limit < 0)
            limit = 10;

        String jsonText = buildJsonText(title, genre, year, director, hidden, limit, offset, orderby, direction);
//        ServiceLogger.LOGGER.info("JSON Text: " + jsonText);

        try {
            requestModel = (SearchMovieRequestModel) ModelValidator.verifyModel(jsonText, SearchMovieRequestModel.class);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, SearchMovieRequestModel.class, email, sessionID, transactionID);
        }

        // Valid request
        boolean nonUserPrivilege = UserRecords.userPrivilegeSufficient(email, 4);

        MovieSearchModel[] movies_from_query = UserRecords.retrieveMoviesFromDB(requestModel, nonUserPrivilege);

        if (movies_from_query.length != 0) {
            int numResults = UserRecords.retrieveNumMovies(requestModel);
            ServiceLogger.LOGGER.info("Retrieved " + numResults + " movies.");
            responseModel = ResponseModelBuilder.Search.constructSearchResponseModel(210, movies_from_query, numResults);
        } else
            responseModel = ResponseModelBuilder.Search.constructResponseModel(211);

        return Response.status(Status.OK).entity(responseModel)
                .header("email", email)
                .header("sessionID", sessionID)
                .header("transactionID", transactionID)
                .build();
    }

    private String buildJsonText(String title, String genre, Integer year, String director, Boolean hidden, Integer limit, Integer offset, String orderby, String direction) {
        String jsonText = "{";

        if (title != null)
            jsonText += String.format("\"title\":\"%s\",", title);
        if (genre != null)
            jsonText += String.format("\"genre\":\"%s\",", genre);
        if (year != null)
            jsonText += String.format("\"year\":%d,", year);
        if (director != null)
            jsonText += String.format("\"director\":\"%s\",", director);
        if (hidden != null)
            jsonText += String.format("\"hidden\":%b,", hidden);

        jsonText += String.format("\"limit\":%d,", limit);
        jsonText += String.format("\"offset\":%d,", offset);
        jsonText += String.format("\"orderby\":\"%s\",", orderby);
        jsonText += String.format("\"direction\":\"%s\"", direction);
        jsonText += "}";

        ServiceLogger.LOGGER.info("buildJsonText successfully ran.");
        return jsonText;
    }

//    private boolean isRequestValid(String email, String sessionID/*, Integer offset, Integer limit, String orderBy, String sortBy*/) {
//        return (email != null) && (sessionID != null)/* && (offset != null) && (limit != null)*/;
//    }
}
