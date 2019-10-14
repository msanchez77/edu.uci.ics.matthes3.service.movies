package edu.uci.ics.matthes3.service.movies.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MovieModel {
    @JsonProperty(required = true)
    private String movieId;
    @JsonProperty(required = true)
    private String title;

    @JsonIgnore
    private String director;
    @JsonIgnore
    private Integer year;
    @JsonIgnore
    private String backdrop_path;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Integer budget;
    @JsonIgnore
    private String overview;
    @JsonIgnore
    private String poster_path;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Integer revenue;
    @JsonProperty(required = true)
    private Float rating;
    @JsonIgnore
    private Integer numVotes;

    @JsonProperty(required = true)
    private GenreSimpleModel[] genres;
    @JsonProperty(required = true)
    private StarSimpleModel[] stars;

    public MovieModel() {
    }

    public MovieModel(String movieId) {
        this.movieId = movieId;
    }

    public MovieModel(
            @JsonProperty(value="movieId", required = true) String movieId,
            @JsonProperty(value="title", required = true) String title,
            @JsonProperty(value="director") String director,
            @JsonProperty(value="year") Integer year,
            @JsonProperty(value="backdrop_path") String backdrop_path,
            @JsonProperty(value="budget") Integer budget,
            @JsonProperty(value="overview") String overview,
            @JsonProperty(value="poster_path") String poster_path,
            @JsonProperty(value="revenue") Integer revenue,
            @JsonProperty(value="rating", required = true) Float rating,
            @JsonProperty(value="numVotes") Integer numVotes,
            @JsonProperty(value="genres", required = true) GenreSimpleModel[] genres,
            @JsonProperty(value="stars", required = true) StarSimpleModel[] stars) {
        this.movieId = movieId;
        this.title = title;
        this.director = director;
        this.year = year;
        this.backdrop_path = backdrop_path;
        this.budget = budget;
        this.overview = overview;
        this.poster_path = poster_path;
        this.revenue = revenue;
        this.rating = rating;
        this.numVotes = numVotes;
        this.genres = genres;
        this.stars = stars;
    }

    public static MovieModel buildModelFromObject(Movie m) {
        return new MovieModel(m.getMovieId(), m.getTitle(), m.getDirector(),
                m.getYear(), m.getBackdrop_path(), m.getBudget(), m.getOverview(),
                m.getPoster_path(), m.getRevenue(), m.getRating(), m.getNumVotes(),
                m.getGenres(), m.getStars());
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

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public Integer getBudget() {
        return budget;
    }

    public String getOverview() {
        return overview;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public Integer getRevenue() {
        return revenue;
    }

    public Float getRating() {
        return rating;
    }

    public Integer getNumVotes() {
        return numVotes;
    }

    public GenreSimpleModel[] getGenres() {
        return genres;
    }

    public StarSimpleModel[] getStars() {
        return stars;
    }
}
