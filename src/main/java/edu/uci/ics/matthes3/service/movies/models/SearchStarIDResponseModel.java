package edu.uci.ics.matthes3.service.movies.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchStarIDResponseModel {
    @JsonProperty(required = true)
    private int resultCode;
    @JsonProperty(required = true)
    private String message;

    @JsonIgnore
    private StarModel star;

    public SearchStarIDResponseModel() {
    }

    @JsonCreator
    public SearchStarIDResponseModel(
            @JsonProperty(value="resultCode", required=true) int resultCode,
            @JsonProperty(value="message", required=true) String message,
            @JsonProperty(value="star") StarModel star) {
        this.resultCode = resultCode;
        this.message = message;
        this.star = star;
    }

    public StarModel getStar() {
        return star;
    }
}
