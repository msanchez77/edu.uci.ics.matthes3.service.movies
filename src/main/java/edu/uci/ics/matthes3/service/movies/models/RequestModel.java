package edu.uci.ics.matthes3.service.movies.models;

public class RequestModel extends RequestModelBase {
    private String email;
    private String sessionID;
    private String transactionID;

    public RequestModel() {
    }

    public RequestModel(String email, String sessionID, String transactionID) {
        this.email = email;
        this.sessionID = sessionID;
        this.transactionID = transactionID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }
}
