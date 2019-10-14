package edu.uci.ics.matthes3.service.movies.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GenreModel {
    @JsonProperty(required = true)
    private int id;
    @JsonProperty(required = true)
    private String name;

    public GenreModel() {
    }

    @JsonCreator
    public GenreModel(
            @JsonProperty(value="id", required = true) int genreId,
            @JsonProperty(value="name", required = true) String name) {
        this.id = genreId;
        this.name = name;
    }

    public static GenreModel buildModelFromObject(Genre g) {
        return new GenreModel(g.getGenreId(), g.getName());
    }

    @Override
    public String toString() {
        return "GenreID: " + id + ", Name: " + name;
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
