package edu.uci.ics.matthes3.service.movies.models;

public class ExistingMovieModel {
    private String movieId;
    private int[] genreId;

    public ExistingMovieModel() {
        this.movieId = null;
        this.genreId = null;
    }

    public ExistingMovieModel(String movieId, int[] genreId) {
        this.movieId = movieId;
        this.genreId = genreId;
    }

    public String getMovieId() {
        return movieId;
    }

    public int[] getGenreId() {
        return genreId;
    }
}
