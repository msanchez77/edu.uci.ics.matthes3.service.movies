package edu.uci.ics.matthes3.service.movies.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class StarSimpleModel {
    @JsonProperty(required = true)
    private String starId;
    @JsonProperty(required = true)
    private String name;
    @JsonIgnore
    private Integer birthYear;

    @JsonCreator
    public StarSimpleModel(
            @JsonProperty(value="id", required = true) String starId,
            @JsonProperty(value="name", required = true) String name,
            @JsonProperty(value="birthYear") Integer birthYear) {
        this.starId = starId;
        this.name = name;
        this.birthYear = birthYear;
    }

    public static StarSimpleModel buildSimpleModelFromObject(StarSimple s) {
        return new StarSimpleModel(s.getStarId(), s.getName(), s.getBirthYear());
    }


    @JsonProperty("id")
    public String getStarId() {
        return starId;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty(value = "birthYear")
    public Integer getBirthYear() {
        return birthYear;
    }
}
