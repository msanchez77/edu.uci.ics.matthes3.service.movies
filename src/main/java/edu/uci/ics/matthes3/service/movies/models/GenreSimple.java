package edu.uci.ics.matthes3.service.movies.models;

import edu.uci.ics.matthes3.service.movies.logger.ServiceLogger;

import java.util.ArrayList;

public class GenreSimple {
    private int genreId;
    private String name;

    public GenreSimple(int genreId, String name) {
        this.genreId = genreId;
        this.name = name;
    }

    public static GenreSimpleModel[] buildGenreArray(ArrayList<GenreSimple> genres) {
        ServiceLogger.LOGGER.info("Creating GenreSimpleModel from list...");

        if (genres == null) {
            ServiceLogger.LOGGER.info("No genres passed to model constructor.");
            return null;
        }

        ServiceLogger.LOGGER.info("Genres list is not empty...");
        int len = genres.size();
        GenreSimpleModel[] array = new GenreSimpleModel[len];

        for (int i = 0; i < len; ++i) {
            ServiceLogger.LOGGER.info("Adding genre " + genres.get(i).getName() + " to array.");
            // Convert each genre in the arraylist to a GenreSimpleModel
            GenreSimpleModel genreSimpleModel = GenreSimpleModel.buildSimpleModelFromObject(genres.get(i));
            array[i] = genreSimpleModel;
        }

        return array;
    }

    public int getGenreId() {
        return genreId;
    }

    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
