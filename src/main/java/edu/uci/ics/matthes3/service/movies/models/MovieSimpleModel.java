package edu.uci.ics.matthes3.service.movies.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MovieSimpleModel {
    @JsonProperty(required = true)
    private String movieId;
    @JsonProperty(required = true)
    private String title;

    public MovieSimpleModel(
            @JsonProperty(value="movieId", required = true) String movieId,
            @JsonProperty(value="title", required = true)String title) {
        this.movieId = movieId;
        this.title = title;
    }

    public static MovieSimpleModel buildSimpleModelFromObject(MovieSimple m) {
        return new MovieSimpleModel(m.getMovieId(), m.getTitle());
    }

    @Override
    public String toString() {
        return "MovieSimpleModel{" +
                "movieId='" + movieId + '\'' +
                ", title='" + title + '\'' +
                '}';
    }

    @JsonProperty("movieId")
    public String getMovieId() {
        return movieId;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }
}
