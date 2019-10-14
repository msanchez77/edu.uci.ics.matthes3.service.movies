package edu.uci.ics.matthes3.service.movies.core;

public class GetEndpoint {

    public static String buildJSONText(String email, String sessionID, String transactionID) {
        return String.format("{\"email\":\"%s\",\"sessionID\":\"%s\",\"transactionID\":\"%s\"}", email, sessionID, transactionID);
    }
}
