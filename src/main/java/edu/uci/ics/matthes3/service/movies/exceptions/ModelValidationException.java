package edu.uci.ics.matthes3.service.movies.exceptions;

public class ModelValidationException extends Exception {
    public ModelValidationException() {
    }

    public ModelValidationException(String message) {
        super(message);
    }

    public ModelValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
