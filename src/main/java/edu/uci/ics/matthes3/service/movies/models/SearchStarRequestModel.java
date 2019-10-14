package edu.uci.ics.matthes3.service.movies.models;

import com.fasterxml.jackson.annotation.*;
import edu.uci.ics.matthes3.service.movies.logger.ServiceLogger;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchStarRequestModel extends RequestModelBase {
    @JsonIgnore
    private String name;
    @JsonIgnore
    private int birthYear;
    @JsonIgnore
    private String movieTitle;

    @JsonProperty(required = true)
    private int offset;
    @JsonProperty(required = true)
    private int limit;
    @JsonProperty(required = true)
    private String orderBy;
    @JsonProperty(required = true)
    private String direction;

    public SearchStarRequestModel() {
    }

    @JsonCreator
    public SearchStarRequestModel(
            @JsonProperty(value="name") String name,
            @JsonProperty(value="birthYear") Integer birthYear,
            @JsonProperty(value="movieTitle") String movieTitle,
            @JsonProperty(value="limit", required=true) int limit,
            @JsonProperty(value="offset", required=true) int offset,
            @JsonProperty(value="orderby", required=true) String orderBy,
            @JsonProperty(value="direction", required=true) String direction) {

        ServiceLogger.LOGGER.info("In non-empty constructor");
        this.name = name;
        this.birthYear = birthYear;
        this.movieTitle = movieTitle;
        this.limit = limit;
        this.offset = offset;
        this.orderBy = orderBy;
        this.direction = direction;
    }

    public String getName() {
        return name;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public int getOffset() {
        return offset;
    }

    public int getLimit() {
        return limit;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public String getDirection() {
        return direction;
    }
}
