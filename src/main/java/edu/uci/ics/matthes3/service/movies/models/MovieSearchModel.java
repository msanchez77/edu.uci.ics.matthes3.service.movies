package edu.uci.ics.matthes3.service.movies.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MovieSearchModel {
    @JsonProperty(required = true)
    private String movieId;
    @JsonProperty(required = true)
    private String title;
    @JsonProperty(required = true)
    private String director;
    @JsonProperty(required = true)
    private Integer year;

    @JsonProperty(required = true)
    private Float rating;
    @JsonProperty(required = true)
    private Integer numVotes;
    @JsonIgnore
    private Integer hidden;

    public MovieSearchModel() {
    }

    public MovieSearchModel(String movieId) {
        this.movieId = movieId;
    }

    public MovieSearchModel(
            @JsonProperty(value="movieId", required=true) String movieId,
            @JsonProperty(value="title", required=true) String title,
            @JsonProperty(value="director", required=true) String director,
            @JsonProperty(value="year", required=true) Integer year,
            @JsonProperty(value="rating", required=true) Float rating,
            @JsonProperty(value="numVotes", required=true) Integer numVotes) {
        this.movieId = movieId;
        this.title = title;
        this.director = director;
        this.year = year;
        this.rating = rating;
        this.numVotes = numVotes;
        this.hidden = null;
    }

    public MovieSearchModel(
            @JsonProperty(value="movieId", required=true) String movieId,
            @JsonProperty(value="title", required=true) String title,
            @JsonProperty(value="director", required=true) String director,
            @JsonProperty(value="year", required=true) Integer year,
            @JsonProperty(value="rating", required=true) Float rating,
            @JsonProperty(value="numVotes", required=true) Integer numVotes,
            @JsonProperty(value="hidden") Boolean hidden) {
        this.movieId = movieId;
        this.title = title;
        this.director = director;
        this.year = year;
        this.rating = rating;
        this.numVotes = numVotes;
        this.hidden = hidden ? 1 : 0;
    }

    public static MovieSearchModel buildModelFromObject(Movie m) {
        if (m.includeHidden != null) {
            return new MovieSearchModel(m.getMovieId(), m.getTitle(), m.getDirector(),
                    m.getYear(), m.getRating(), m.getNumVotes(), m.getIncludeHidden());
        } else {
            return new MovieSearchModel(m.getMovieId(), m.getTitle(), m.getDirector(),
                    m.getYear(), m.getRating(), m.getNumVotes());
        }
    }


    public String getMovieId() {
        return movieId;
    }

    public String getTitle() {
        return title;
    }

    public String getDirector() {
        return director;
    }

    public Integer getYear() {
        return year;
    }

    public Float getRating() {
        return rating;
    }

    public Integer getNumVotes() {
        return numVotes;
    }

    public Integer getHidden() {
        return hidden;
    }
}
