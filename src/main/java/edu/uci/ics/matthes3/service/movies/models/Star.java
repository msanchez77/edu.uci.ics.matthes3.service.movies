package edu.uci.ics.matthes3.service.movies.models;

import edu.uci.ics.matthes3.service.movies.logger.ServiceLogger;

import java.util.ArrayList;

public class Star {
    private String id;
    private String name;
    private Integer birthYear;

    public Star() {
    }

    public Star(String id, String name, Integer birthYear) {
        this.id = id;
        this.name = name;
        this.birthYear = birthYear;
    }

    public static StarModel[] buildStarArray(ArrayList<Star> stars) {
        ServiceLogger.LOGGER.info("Creating Star array from list...");

        if (stars == null) {
            ServiceLogger.LOGGER.info("No stars passed to model constructor.");
            return null;
        }

        ServiceLogger.LOGGER.info("Stars list is not empty...");
        int len = stars.size();
        StarModel[] array = new StarModel[len];

        for (int i = 0; i < len; ++i) {
            ServiceLogger.LOGGER.info("Adding star " + stars.get(i).getName() + " to array.");
            // Convert each star in the arraylist to a StarModel
            StarModel starModel = StarModel.buildModelFromObject(stars.get(i));
            array[i] = starModel;
        }
//        ServiceLogger.LOGGER.info("Finished building model. Array of movie contains: ");
//        for (MovieModel movieModel : array) {
//            ServiceLogger.LOGGER.info("\t" + movieModel);
//        }

        return array;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }
}
