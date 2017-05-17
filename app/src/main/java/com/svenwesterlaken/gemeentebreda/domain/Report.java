package com.svenwesterlaken.gemeentebreda.domain;

import android.icu.util.ULocale;

/**
 * Created by lukab on 9-5-2017.
 */

public class Report {

    int reportID, locationID, categoryID, userID;
    String report;
    User user;
    Location location;
    String description;
    Media media;
    Category category;

    public Report() {
    }

    public Report(int reportID, User user, Location location, String description, Category category, int locationID, String report) {
        this.reportID = reportID;
        this.category = category;
        this.user = user;
        this.location = location;
        this.description = description;
        this.category = category;
        this.locationID = locationID;
        this.report = report;
    }



    //getters


    public int getLocationID() {
        return locationID;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public int getUserID() {
        return userID;
    }

    public int getReportID() {
        return reportID;
    }

    public User getUser() {
        return user;
    }

    public Location getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public Media getMedia() {
        return media;
    }

    public Category getCategory() {
        return category;
    }

    public String getReport(){return report;}

    //setters


    public void setReportID(int reportID) {
        this.reportID = reportID;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setLocationID(int locationID) {
        this.locationID = locationID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setReport(String report) {this.report = report;}
}
