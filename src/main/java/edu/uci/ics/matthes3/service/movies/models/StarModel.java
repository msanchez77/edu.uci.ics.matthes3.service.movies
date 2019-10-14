package edu.uci.ics.matthes3.service.movies.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class StarModel {
    @JsonProperty(required = true)
    private String id;
    @JsonProperty(required = true)
    private String name;
    @JsonIgnore
    private Integer birthYear;

    public StarModel() {
    }

    @JsonCreator
    public StarModel(
            @JsonProperty(value="id", required = true) String id,
            @JsonProperty(value="name", required = true) String name,
            @JsonProperty(value="birthYear") Integer birthYear) {
        this.id = id;
        this.name = name;
        this.birthYear = birthYear;
    }

    public static StarModel buildModelFromObject(Star s) {
        return new StarModel(s.getId(), s.getName(), s.getBirthYear());
    }

    @JsonProperty("id")
    public String getId() {
        return id;
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