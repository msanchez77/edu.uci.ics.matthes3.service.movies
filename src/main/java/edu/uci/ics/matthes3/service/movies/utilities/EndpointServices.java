package edu.uci.ics.matthes3.service.movies.utilities;

import edu.uci.ics.matthes3.service.movies.core.UserRecords;
import edu.uci.ics.matthes3.service.movies.logger.ServiceLogger;
import edu.uci.ics.matthes3.service.movies.models.VerifyPrivilegeResponseModel;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

public class EndpointServices {

    public static String[] getHeaders(HttpHeaders headers) {
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");
        ServiceLogger.LOGGER.info("EMAIL: " + email);
        ServiceLogger.LOGGER.info("SESSIONID: " + sessionID);
        ServiceLogger.LOGGER.info("TRANSACTIONID: " + transactionID);

        return new String[]{email, sessionID, transactionID};
    }

    public static VerifyPrivilegeResponseModel verifyPrivilege(String email) {
        VerifyPrivilegeResponseModel privilegeResponseModel;
        boolean privilegeSufficient = UserRecords.userPrivilegeSufficient(email, 3);
        if (!privilegeSufficient) {
            ResultCodes resultCodes = new ResultCodes();

            privilegeResponseModel = new VerifyPrivilegeResponseModel(
                    141,
                    resultCodes.setMessage(141)
            );

            return privilegeResponseModel;
        }

        return null;
    }
}
