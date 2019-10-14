package edu.uci.ics.matthes3.service.movies.models;

import edu.uci.ics.matthes3.service.movies.logger.ServiceLogger;

import java.util.ArrayList;

public class StarSimple {
    private String starId;
    private String name;
    private Integer birthYear;

    public StarSimple(String starId, String name, Integer birthYear) {
        this.starId = starId;
        this.name = name;
        this.birthYear = birthYear;
    }


    public static StarSimpleModel[] buildStarArray(ArrayList<StarSimple> stars) {
        ServiceLogger.LOGGER.info("Creating StarSimpleModel from list...");

        if (stars == null) {
            ServiceLogger.LOGGER.info("No stars passed to model constructor.");
            return null;
        }

        ServiceLogger.LOGGER.info("Stars list is not empty...");
        int len = stars.size();
        StarSimpleModel[] array = new StarSimpleModel[len];

        for (int i = 0; i < len; ++i) {
            ServiceLogger.LOGGER.info("Adding star " + stars.get(i).getName() + " to array.");
            // Convert each star in the arraylist to a StarSimpleModel
            StarSimpleModel starSimpleModel = StarSimpleModel.buildSimpleModelFromObject(stars.get(i));
            array[i] = starSimpleModel;
        }

        return array;
    }

    public String getStarId() {
        return starId;
    }

    public void setStarId(String starId) {
        this.starId = starId;
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
