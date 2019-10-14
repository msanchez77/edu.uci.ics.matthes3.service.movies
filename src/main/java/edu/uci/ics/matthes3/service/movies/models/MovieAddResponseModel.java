package edu.uci.ics.matthes3.service.movies.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MovieAddResponseModel {
    @JsonProperty(required = true)
    private int resultCode;
    @JsonProperty(required = true)
    private String message;
    @JsonIgnore
    private String movieid;
    @JsonIgnore
    private int[] genreid;

    public MovieAddResponseModel() {
    }

    @JsonCreator
    public MovieAddResponseModel(
            @JsonProperty(value="resultCode", required=true) int resultCode,
            @JsonProperty(value="message", required=true) String message,
            @JsonProperty(value="movieid") String movieid,
            @JsonProperty(value="genreid") int[] genreid) {
        this.resultCode = resultCode;
        this.message = message;
        this.movieid = movieid;
        this.genreid = genreid;
    }

    @Override
    public String toString() {
        return "MovieAddResponseModel{" +
                "resultCode=" + resultCode +
                ", message='" + message + '\'' +
                ", movieid='" + movieid + '\'' +
                ", genreid=" + Arrays.toString(genreid) +
                '}';
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMovieid() {
        return movieid;
    }

    public void setMovieid(String movieid) {
        this.movieid = movieid;
    }

    public int[] getGenreid() {
        return genreid;
    }

    public void setGenreid(int[] genreid) {
        this.genreid = genreid;
    }
}
