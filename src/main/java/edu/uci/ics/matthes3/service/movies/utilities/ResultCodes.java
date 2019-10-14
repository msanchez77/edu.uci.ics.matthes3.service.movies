package edu.uci.ics.matthes3.service.movies.utilities;

import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

public class ResultCodes {
    public ResultCodes() {
    }

    public static String setMessage(int resultCode) {
        if (resultCode == -17)
            return "SessionID not provided in request header.";
        if (resultCode == -16)
            return "Email not provided in request header.";
        if (resultCode == -15)
            return "User ID number is out of valid range.";
        if (resultCode == -14)
            return "Privilege level out of valid range.";
        if (resultCode == -13)
            return "Token has invalid length.";
        if (resultCode == -12)
            return "Password has invalid length.";
        if (resultCode == -11)
            return "Email has invalid format.";
        if (resultCode == -10)
            return "Email has invalid length.";
        if (resultCode == -3)
            return "JSON parse Exception.";
        if (resultCode == -2)
            return "JSON mapping Exception.";
        if (resultCode == -1)
            return "Internal server error.";
        if (resultCode == 11)
            return "Passwords do not match.";
        if (resultCode == 12)
            return "Password does not meet length requirements.";
        if (resultCode == 13)
            return "Password does not meet character requirements.";
        if (resultCode == 14)
            return "User not found.";
        if (resultCode == 16)
            return "Email already in use.";
        if (resultCode == 110)
            return "User registered successfully.";
        if (resultCode == 120)
            return "User logged in successfully.";
        if (resultCode == 130)
            return "Session is active.";
        if (resultCode == 131)
            return "Session is expired.";
        if (resultCode == 132)
            return "Session is closed.";
        if (resultCode == 133)
            return "Session is revoked.";
        if (resultCode == 140)
            return "User has sufficient privilege level.";
        if(resultCode == 141)
            return "User has insufficient privilege level.";
        if(resultCode == 150)
            return "Password updated successfully.";
        if(resultCode == 160)
            return "User successfully retrieved.";
        if(resultCode == 170)
            return "User Created.";
        if(resultCode == 171)
            return "Creating user with \"ROOT\" privilege is not allowed.";
        if(resultCode == 180)
            return "User updated.";
        if(resultCode == 181)
            return "Users cannot be elevated to root privilege level.";
        if(resultCode == 210)
            return "Found movies with search parameters.";
        if(resultCode == 211)
            return "No movies found with search parameters.";
        if(resultCode == 212)
            return "Found stars with search parameters.";
        if(resultCode == 213)
            return "No stars found with search parameters.";
        if(resultCode == 214)
            return "Movie successfully added.";
        if(resultCode == 215)
            return "Could not add movie.";
        if(resultCode == 216)
            return "Movie already exists.";
        if(resultCode == 217)
            return "GenreDB successfully added.";
        if(resultCode == 218)
            return "GenreDB could not be added.";
        if (resultCode == 219)
            return "Genres successfully retrieved.";
        if (resultCode == 220)
            return "Star successfully added.";
        if (resultCode == 221)
            return "Could not add star.";
        if (resultCode == 222)
            return "Star already exists.";
        if (resultCode == 230)
            return "Star successfully added to movie.";
        if (resultCode == 231)
            return "Could not add star to movie.";
        if (resultCode == 232)
            return "Star already exists in movie.";
        if (resultCode == 240)
            return "Movie successfully removed.";
        if (resultCode == 241)
            return "Could not remove movie.";
        if (resultCode == 242)
            return "Movie has been already removed.";
        if (resultCode == 250)
            return "Rating successfully updated.";
        if (resultCode == 251)
            return "Could not update rating.";
        return "INVALID RESULT CODE";
    }

    public Response.Status mapCodetoStatus(int resultCode) {
        if (resultCode == -1)
            return Response.Status.INTERNAL_SERVER_ERROR;
        else if (resultCode < -1)
            return Response.Status.BAD_REQUEST;
        else
            return Response.Status.OK;
    }
}
