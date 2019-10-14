package edu.uci.ics.matthes3.service.movies.models;

import edu.uci.ics.matthes3.service.movies.logger.ServiceLogger;

import java.util.ArrayList;

public class Movie {
    private String movieId;
    private String title;
    private String director;
    private Integer year;
    private String backdrop_path;
    private Integer budget;
    private String overview;
    private String poster_path;
    private Integer revenue;
    private Float rating;
    private Integer numVotes;
    public Boolean includeHidden;
    private GenreSimpleModel[] genres;
    private StarSimpleModel[] stars;

    public Movie() {
    }

    public Movie(String movieId, String title, String director,
                 Integer year, Float rating, Integer numVotes, Boolean includeHidden) {
        this.movieId = movieId;
        this.title = title;
        this.director = director;
        this.year = year;
        this.rating = rating;
        this.numVotes = numVotes;
        this.includeHidden = includeHidden;
    }

    public Movie(String movieId, String title, String director,
                 Integer year, String backdrop_path, Integer budget,
                 String overview, String poster_path, Integer revenue,
                 Float rating, Integer numVotes,
                 GenreSimpleModel[] genres, StarSimpleModel[] stars) {
        this.movieId = movieId;
        this.title = title;
        this.director = director;
        this.year = year;
        this.backdrop_path = backdrop_path;
        this.budget = budget;
        this.overview = overview;
        this.poster_path = poster_path;
        this.revenue = revenue;
        this.rating = rating;
        this.numVotes = numVotes;
        this.genres = genres;
        this.stars = stars;
    }

    public static MovieModel[] buildMovieArray(ArrayList<Movie> movies) {
        ServiceLogger.LOGGER.info("Creating SearchMovieResponseModel from list...");

        if (movies == null) {
            ServiceLogger.LOGGER.info("No movies passed to model constructor.");
            return null;
        }

        ServiceLogger.LOGGER.info("Movies list is not empty...");
        int len = movies.size();
        MovieModel[] array = new MovieModel[len];

        for (int i = 0; i < len; ++i) {
            ServiceLogger.LOGGER.info("Adding movie " + movies.get(i).getTitle() + " to array.");
            // Convert each movie in the arraylist to a MovieModel
            MovieModel movieModel = MovieModel.buildModelFromObject(movies.get(i));
            array[i] = movieModel;
        }

        return array;
    }

    public static MovieSearchModel[] buildMovieSearchArray(ArrayList<Movie> movies) {
        ServiceLogger.LOGGER.info("Creating SearchMovieResponseModel from list...");

        if (movies == null) {
            ServiceLogger.LOGGER.info("No movies passed to model constructor.");
            return null;
        }

        ServiceLogger.LOGGER.info("Movies list is not empty...");
        int len = movies.size();
        MovieSearchModel[] array = new MovieSearchModel[len];

        for (int i = 0; i < len; ++i) {
            ServiceLogger.LOGGER.info("Adding movie " + movies.get(i).getTitle() + " to array.");
            // Convert each movie in the arraylist to a MovieModel
            MovieSearchModel movieSearchModel = MovieSearchModel.buildModelFromObject(movies.get(i));
            array[i] = movieSearchModel;
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

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public Integer getBudget() {
        return budget;
    }

    public void setBudget(Integer budget) {
        this.budget = budget;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public Integer getRevenue() {
        return revenue;
    }

    public void setRevenue(Integer revenue) {
        this.revenue = revenue;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public Integer getNumVotes() {
        return numVotes;
    }

    public void setNumVotes(Integer numVotes) {
        this.numVotes = numVotes;
    }

    public Boolean getIncludeHidden() {
        return includeHidden;
    }

    public void setIncludeHidden(Boolean includeHidden) {
        this.includeHidden = includeHidden;
    }

    public GenreSimpleModel[] getGenres() {
        return genres;
    }

    public void setGenres(GenreSimpleModel[] genres) {
        this.genres = genres;
    }

    public StarSimpleModel[] getStars() {
        return stars;
    }

    public void setStars(StarSimpleModel[] stars) {
        this.stars = stars;
    }
}
