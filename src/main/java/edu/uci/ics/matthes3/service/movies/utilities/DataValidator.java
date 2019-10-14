package edu.uci.ics.matthes3.service.movies.utilities;

import edu.uci.ics.matthes3.service.movies.MovieService;
import edu.uci.ics.matthes3.service.movies.logger.ServiceLogger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataValidator {
    public static class Emails {
        public static final int MAX_EMAIL_SIZE = 50;

        // CASE -11
        public static boolean isFormatValid(String email) {
            return email.matches("^[A-z0-9_.\\-]+@[A-z0-9]+\\.[a-z]{2,4}$");
        }

        // CASE -10
        public static boolean isLengthValid(String email) {
            String[] emailName = email.split("@");

            return (emailName[0].length() <= MAX_EMAIL_SIZE);
        }

        // CASE 16
        public static boolean isUnique(String email, int num) {
            String query = "SELECT COUNT(*) " +
                    "FROM users " +
                    "WHERE email LIKE ? ";

            return checkEqualityQuery(query, email, num);
        }
    }

    public static class Passwords {
        public static final int MIN_PASSWORD_LENGTH = 7;
        public static final int MAX_PASSWORD_LENGTH = 16;
        // CASE -12
        public static boolean isEmptyOrNull(char[] password) {
            boolean bool = (password != null && password.length != 0);
            password = null;
            return bool;
        }

        // CASE 12
        public static boolean isLengthValid(char[] password) {
            boolean bool = password.length >= MIN_PASSWORD_LENGTH && password.length <= MAX_PASSWORD_LENGTH;
            password = null;
            return bool;
        }

        // CASE 13
        public static boolean isFormatValid(char[] password) {
            // At least one upper case alpha
            boolean upperCase = false;
            // At least one lower case alpha
            boolean lowerCase = false;
            // At least one numeric
            boolean numeric = false;
            // At least one special character
            boolean specialChar = false;

            for (char c: password) {
                if (Character.isLowerCase(c)) {
                    lowerCase = true;
                    continue;
                }
                if (Character.isUpperCase(c)) {
                    upperCase = true;
                    continue;
                }
                if (Character.isDigit(c)) {
                    numeric = true;
                    continue;
                }
                specialChar = isSpecial(c);

            }

            return upperCase && lowerCase && numeric && specialChar;
        }

        private static boolean isSpecial(char c) {
            String specials = " !\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";
            for (int i = 0; i < specials.length(); i++) {
                if (c == specials.charAt(i)) {
                    return true;
                }
            }
            return false;
        }
    }

    public static class Privilege {
        public static final int MIN_PLEVEL = 2;
        public static final int MAX_PLEVEL = 5;
        public static final int PLEVEL_ADMIN = 2;
        public static final int PLEVEL_EMPLOYEE = 3;
        public static final int PLEVEL_SERVICE = 4;
        public static final int PLEVEL_USER = 5;
        public static final int PLEVEL_DEFAULT = PLEVEL_USER;

        public static boolean isCorrectRange(int plevel) {
            return (plevel <= MAX_PLEVEL) && (plevel >= MIN_PLEVEL);
        }

        public static boolean isSufficient(String email, int plevel) {
            String query = "SELECT plevel " +
                    "FROM users " +
                    "WHERE email LIKE ? ";

            return checkEqualityQuery(query, email, plevel);
        }
    }

    private static boolean checkEqualityQuery(String query, String email, int num) {
        try {
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ps.setString(1, email);

            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");

            int n = 0;
            while (rs.next())
                n = rs.getInt(1);

            return n == num;
        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: Unable to retrieve records.");
            e.printStackTrace();

            return false;
        }
    }
}
