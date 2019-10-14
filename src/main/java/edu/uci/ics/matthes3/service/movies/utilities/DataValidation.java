package edu.uci.ics.matthes3.service.movies.utilities;

public interface DataValidation {
    class DataValidationException extends Exception {
        public DataValidationException(String s) {
            super(s);
        }
    }

    boolean isDataValid() throws DataValidationException;
}
