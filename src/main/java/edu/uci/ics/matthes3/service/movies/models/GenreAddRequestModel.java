package edu.uci.ics.matthes3.service.movies.models;

public class GenreAddRequestModel extends RequestModelBase {
    private String name;

    public GenreAddRequestModel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
