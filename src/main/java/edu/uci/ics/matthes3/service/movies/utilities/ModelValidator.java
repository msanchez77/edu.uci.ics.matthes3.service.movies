package edu.uci.ics.matthes3.service.movies.utilities;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.matthes3.service.movies.exceptions.ModelValidationException;
import edu.uci.ics.matthes3.service.movies.logger.ServiceLogger;
import edu.uci.ics.matthes3.service.movies.models.Model;
import edu.uci.ics.matthes3.service.movies.models.VerifyPrivilegeResponseModel;
import org.glassfish.jersey.internal.util.ExceptionUtils;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;
import java.lang.reflect.Constructor;


import static edu.uci.ics.matthes3.service.movies.utilities.ResultCodes.*;

public class ModelValidator {

    public static final int INTERNAL_SERVER_ERROR = -1;
    public static final int JSON_MAPPING_EXCEPTION = -2;
    public static final int JSON_PARSE_EXCEPTION = -3;

    public static Model verifyModel(String jsonText, Class modelType) throws ModelValidationException {
        ServiceLogger.LOGGER.info("Verifying model format...");
        ObjectMapper mapper = new ObjectMapper();
        String warning = "";
        Model model;

        try {
            ServiceLogger.LOGGER.info("Attempting to deserialize JSON to POJO");
            model = (Model) mapper.readValue(jsonText, modelType);
            ServiceLogger.LOGGER.info("Successfully deserialized JSON to POJO.");
        } catch (JsonMappingException e) {
            warning = "Unable to map JSON to POJO--request has invalid format.";
            ServiceLogger.LOGGER.warning(warning + "\n" + ExceptionUtils.exceptionStackTraceAsString(e));
            throw new ModelValidationException(warning, e);
        } catch (JsonParseException e) {
            warning = "Unable to parse JSON--text is not in valid JSON format.";
            ServiceLogger.LOGGER.warning(warning + "\n" + ExceptionUtils.exceptionStackTraceAsString(e));
            throw new ModelValidationException(warning, e);
        } catch (IOException e) {
            warning = "IOException while mapping JSON to POJO.";
            ServiceLogger.LOGGER.warning(warning + "\n" + ExceptionUtils.exceptionStackTraceAsString(e));
            throw new ModelValidationException(warning, e);
        }
        ServiceLogger.LOGGER.info("Model has been validated.");
        return model;
    }

    public static Response returnInvalidRequest(ModelValidationException e, Class modelType, String email, String sessionID, String transactionID) {
        try {
            Class<?> model = Class.forName(modelType.getName());
            Constructor<?> constructor;
            constructor = model.getConstructor(Integer.TYPE);
            Object object = null;
            int resultCode;

            if (e.getCause() instanceof JsonMappingException) {
                object = constructor.newInstance(JSON_MAPPING_EXCEPTION);
                resultCode = JSON_MAPPING_EXCEPTION;
            } else if (e.getCause() instanceof JsonParseException) {
                object = constructor.newInstance(JSON_PARSE_EXCEPTION);
                resultCode = JSON_PARSE_EXCEPTION;
            } else {
                object = constructor.newInstance(INTERNAL_SERVER_ERROR);
                resultCode = INTERNAL_SERVER_ERROR;
            }
            if (resultCode > 0) {
                return Response.status(Status.OK).entity(object).build();
            } else if (resultCode < -1){
                return Response.status(Status.BAD_REQUEST).entity(object)
                        .header("email", email)
                        .header("sessionID", sessionID)
                        .header("transactionID", transactionID)
                        .build();
            } else {
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(object)
                        .header("email", email)
                        .header("sessionID", sessionID)
                        .header("transactionID", transactionID)
                        .build();
            }

        } catch (Exception ex) {
            ServiceLogger.LOGGER.warning("Unable to create ResponseModel " + modelType.getName());
            ServiceLogger.LOGGER.warning(ExceptionUtils.exceptionStackTraceAsString(e));
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
