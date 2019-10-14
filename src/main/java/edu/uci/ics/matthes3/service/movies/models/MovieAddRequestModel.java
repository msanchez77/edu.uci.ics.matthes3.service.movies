package edu.uci.ics.matthes3.service.movies.models;

import com.fasterxml.jackson.annotation.*;
import edu.uci.ics.matthes3.service.movies.logger.ServiceLogger;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MovieAddRequestModel extends RequestModelBase {
    @JsonProperty(required = true)
    private String title;
    @JsonProperty(required = true)
    private String director;
    @JsonProperty(required = true)
    private Integer year;

    @JsonIgnore
    private String backdrop_path;
    @JsonIgnore
    private Integer budget;
    @JsonIgnore
    private String overview;
    @JsonIgnore
    private String poster_path;
    @JsonIgnore
    private Integer revenue;

    @JsonProperty(required = true)
    private GenreModel[] genres;

    public MovieAddRequestModel() {
        ServiceLogger.LOGGER.info("In empty constructor.");
    }

    @JsonCreator
    public MovieAddRequestModel(
            @JsonProperty(value="title", required=true)String title,
            @JsonProperty(value="director", required=true)String director,
            @JsonProperty(value="year", required=true)Integer year,
            @JsonProperty(value="backdrop_path") String backdrop_path,
            @JsonProperty(value="budget") Integer budget,
            @JsonProperty(value="overview") String overview,
            @JsonProperty(value="poster_path") String poster_path,
            @JsonProperty(value="revenue") Integer revenue,
            @JsonProperty(value="genres", required=true) GenreModel[] genres) {
        ServiceLogger.LOGGER.info("In non-empty constructor");
        this.title = title;
        this.director = director;
        this.year = year;
        this.backdrop_path = backdrop_path;
        this.budget = budget;
        this.overview = overview;
        this.poster_path = poster_path;
        this.revenue = revenue;
        this.genres = genres;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("director")
    public String getDirector() {
        return director;
    }

    @JsonProperty(value = "year")
    public Integer getYear() {
        return year;
    }

    @JsonProperty(value = "backdrop_path")
    public String getBackdrop_path() {
        return backdrop_path;
    }

    @JsonProperty(value = "budget")
    public Integer getBudget() {
        return budget;
    }

    @JsonProperty(value = "overview")
    public String getOverview() {
        return overview;
    }

    @JsonProperty(value = "poster_path")
    public String getPoster_path() {
        return poster_path;
    }

    @JsonProperty(value = "revenue")
    public Integer getRevenue() {
        return revenue;
    }

    @JsonProperty("genres")
    public GenreModel[] getGenres() {
        return genres;
    }
}
