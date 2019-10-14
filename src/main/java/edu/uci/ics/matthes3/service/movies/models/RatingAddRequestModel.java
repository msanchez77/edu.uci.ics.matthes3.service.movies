package edu.uci.ics.matthes3.service.movies.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RatingAddRequestModel extends RequestModelBase {
    @JsonProperty(required = true)
    private String id;
    @JsonProperty(required = true)
    private Float rating;

    @JsonCreator
    public RatingAddRequestModel(
            @JsonProperty(value = "id", required = true) String id,
            @JsonProperty(value = "rating", required = true) Float rating) {
        this.id = id;
        this.rating = rating;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("rating")
    public Float getRating() {
        return rating;
    }
}
