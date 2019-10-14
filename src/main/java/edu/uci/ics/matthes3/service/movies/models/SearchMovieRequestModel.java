package edu.uci.ics.matthes3.service.movies.models;

import com.fasterxml.jackson.annotation.*;
//import edu.uci.ics.matthes3.service.movies.MovieService;
//import edu.uci.ics.matthes3.service.movies.configs.IDMConfigs;
import edu.uci.ics.matthes3.service.movies.logger.ServiceLogger;
//import edu.uci.ics.matthes3.service.movies.utilities.DataValidation;
//import edu.uci.ics.matthes3.service.movies.utilities.DataValidator;
//
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchMovieRequestModel extends RequestModelBase {
    @JsonIgnore
    private String title;
    @JsonIgnore
    private String genre;
    @JsonIgnore
    private Integer year;
    @JsonIgnore
    private String director;
    @JsonIgnore
    private Boolean hidden;
    @JsonIgnore
    private Integer limit;
    @JsonIgnore
    private Integer offset;
    @JsonIgnore
    private String orderby;
    @JsonIgnore
    private String direction;

    public SearchMovieRequestModel() {
        ServiceLogger.LOGGER.info("In empty constructor.");
    }

    @JsonCreator
    public SearchMovieRequestModel(
            @JsonProperty(value="title")String title,
            @JsonProperty(value="genre")String genre,
            @JsonProperty(value="year")Integer year,
            @JsonProperty(value="director")String director,
            @JsonProperty(value="hidden")Boolean hidden,
            @JsonProperty(value="limit", required=true) Integer limit,
            @JsonProperty(value="offset", required=true) Integer offset,
            @JsonProperty(value="orderby", required=true) String orderby,
            @JsonProperty(value="direction", required=true) String direction) {
        ServiceLogger.LOGGER.info("In non-empty constructor");
        this.title = title;
        this.genre = genre;
        this.year = year;
        this.director = director;
        this.hidden = hidden;
        this.limit = limit;
        this.offset = offset;
        this.orderby = orderby;
        this.direction = direction;
    }

    @JsonProperty(value = "title")
    public String getTitle() {
        return title;
    }

    @JsonProperty(value = "genre")
    public String getGenre() {
        return genre;
    }

    @JsonProperty(value = "year")
    public Integer getYear() {
        return year;
    }

    @JsonProperty(value = "director")
    public String getDirector() {
        return director;
    }

    @JsonProperty(value = "hidden")
    public Boolean getHidden() {
        return hidden;
    }

    @JsonProperty(value = "limit")
    public Integer getLimit() {
        return limit;
    }

    @JsonProperty(value = "offset")
    public Integer getOffset() {
        return offset;
    }

    @JsonProperty(value = "orderby")
    public String getOrderby() {
        return orderby;
    }

    @JsonProperty(value = "direction")
    public String getDirection() {
        return direction;
    }
}
