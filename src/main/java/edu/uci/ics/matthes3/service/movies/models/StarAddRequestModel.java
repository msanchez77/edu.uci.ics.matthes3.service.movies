package edu.uci.ics.matthes3.service.movies.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class StarAddRequestModel extends RequestModelBase {
    @JsonProperty(required = true)
    private String name;
    @JsonIgnore
    private Integer birthYear;

    @JsonCreator
    public StarAddRequestModel(
            @JsonProperty(value="name", required=true) String name,
            @JsonProperty(value="birthYear") Integer birthYear) {
        this.name = name;
        this.birthYear = birthYear;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("birthYear")
    public Integer getBirthYear() {
        return birthYear;
    }
}
