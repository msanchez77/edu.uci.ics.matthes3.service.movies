package edu.uci.ics.matthes3.service.movies.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.matthes3.service.movies.logger.ServiceLogger;

import java.util.ArrayList;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchMovieResponseModel extends ResponseModel {
    @JsonIgnore
    private MovieSearchModel[] movies;
    @JsonIgnore
    private Integer numResults;

    public SearchMovieResponseModel() {
    }

    public SearchMovieResponseModel(
            @JsonProperty(value="resultCode", required=true) int resultCode,
            @JsonProperty(value="message", required=true) String message) {
        super(resultCode, message);
    }

    public SearchMovieResponseModel(
            @JsonProperty(value="resultCode", required=true) int resultCode,
            @JsonProperty(value="message", required=true) String message,
            @JsonProperty(value="movies") MovieSearchModel[] movies,
            @JsonProperty(value="numResults") Integer numResults) {
        super(resultCode, message);
        this.movies = movies;
        this.numResults = numResults;
    }

    public MovieSearchModel[] getMovies() {
        return movies;
    }

    public Integer getNumResults() {
        return numResults;
    }
}
