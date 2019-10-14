package edu.uci.ics.matthes3.service.movies.models;

import edu.uci.ics.matthes3.service.movies.logger.ServiceLogger;

import java.util.ArrayList;

public class Genre {
    private int genreId;
    private String name;

    public Genre() {
    }

    public Genre(int genreId, String name) {
        this.genreId = genreId;
        this.name = name;
    }

    public static GenreModel[] buildGenreArray(ArrayList<Genre> genres) {
        ServiceLogger.LOGGER.info("Creating GenreDB array from list...");

        if (genres == null) {
            ServiceLogger.LOGGER.info("No genres passed to model constructor.");
            return null;
        }

        ServiceLogger.LOGGER.info("Genres list is not empty...");
        int len = genres.size();
        GenreModel[] array = new GenreModel[len];

        for (int i = 0; i < len; ++i) {
            ServiceLogger.LOGGER.info("Adding genre " + genres.get(i).getName() + " to array.");
            // Convert each genre in the arraylist to a GenreModel
            GenreModel genreModel = GenreModel.buildModelFromObject(genres.get(i));
            array[i] = genreModel;
        }
//        ServiceLogger.LOGGER.info("Finished building model. Array of movie contains: ");
//        for (MovieModel movieModel : array) {
//            ServiceLogger.LOGGER.info("\t" + movieModel);
//        }

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
