package edu.uci.ics.matthes3.service.movies.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchMovieIDResponseModel {
    @JsonProperty(required = true)
    private int resultCode;
    @JsonProperty(required = true)
    private String message;

    @JsonIgnore
    private MovieModel movie;

    public SearchMovieIDResponseModel() {
    }

    @JsonCreator
    public SearchMovieIDResponseModel(
            @JsonProperty(value="resultCode", required=true) int resultCode,
            @JsonProperty(value="message", required=true) String message) {
        this.resultCode = resultCode;
        this.message = message;
    }

    @JsonCreator
    public SearchMovieIDResponseModel(
            @JsonProperty(value="resultCode", required=true) int resultCode,
            @JsonProperty(value="message", required=true) String message,
            @JsonProperty(value="movie") MovieModel movie) {
        this.resultCode = resultCode;
        this.message = message;
        this.movie = movie;
    }

    public MovieModel getMovie() {
        return movie;
    }
}
