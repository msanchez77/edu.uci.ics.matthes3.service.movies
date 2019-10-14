package edu.uci.ics.matthes3.service.movies.models;

import edu.uci.ics.matthes3.service.movies.logger.ServiceLogger;

import java.util.ArrayList;
import java.util.Arrays;

public class MovieSimple {
    private String movieId;
    private String title;

    public MovieSimple(String movieId, String title) {
        this.movieId = movieId;
        this.title = title;
    }

    @Override
    public String toString() {
        return "MovieSimple{" +
                "movieId='" + movieId + '\'' +
                ", title='" + title + '\'' +
                '}';
    }

    public static MovieSimpleModel[] buildMovieArray(ArrayList<MovieSimple> movies) {
        ServiceLogger.LOGGER.info("Creating MovieSimpleModel from list...");

        if (movies == null) {
            ServiceLogger.LOGGER.info("No movies passed to model constructor.");
            return null;
        }

        ServiceLogger.LOGGER.info("Movies list is not empty...");
        int len = movies.size();
        MovieSimpleModel[] array = new MovieSimpleModel[len];

        for (int i = 0; i < len; ++i) {
            ServiceLogger.LOGGER.info("Adding movie " + movies.get(i).getTitle() + " to array.");
            // Convert each movie in the arraylist to a MovieModel
            MovieSimpleModel movieModel = MovieSimpleModel.buildSimpleModelFromObject(movies.get(i));
            array[i] = movieModel;
        }

        return array;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
