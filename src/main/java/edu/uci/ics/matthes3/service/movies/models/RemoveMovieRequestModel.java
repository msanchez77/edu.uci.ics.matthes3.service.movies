package edu.uci.ics.matthes3.service.movies.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RemoveMovieRequestModel {
    @JsonProperty(required = true)
    private String id;

    public RemoveMovieRequestModel(
            @JsonProperty(value="id", required=true)String id) {
        this.id = id;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }
}
