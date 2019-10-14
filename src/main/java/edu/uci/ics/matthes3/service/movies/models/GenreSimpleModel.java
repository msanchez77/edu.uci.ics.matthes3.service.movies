package edu.uci.ics.matthes3.service.movies.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GenreSimpleModel {
    @JsonProperty(required = true)
    private int id;
    @JsonProperty(required = true)
    private String name;

    @JsonCreator
    public GenreSimpleModel(
            @JsonProperty(value="id", required = true)int id,
            @JsonProperty(value="name", required = true)String name) {
        this.id = id;
        this.name = name;
    }

    public static GenreSimpleModel buildSimpleModelFromObject(GenreSimple g) {
        return new GenreSimpleModel(g.getGenreId(), g.getName());
    }

    @JsonProperty("id")
    public int getGenreId() {
        return id;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }
}
