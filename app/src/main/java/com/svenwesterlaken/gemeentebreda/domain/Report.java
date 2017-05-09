package com.svenwesterlaken.gemeentebreda.domain;

import android.icu.util.ULocale;

/**
 * Created by lukab on 9-5-2017.
 */

public class Report {

    int reportID;
    User user;
    Location location;
    String description;
    Media media;
    Category category;

    public Report(int reportID, User user, Location location, String description, Media media, Category category) {
        this.reportID = reportID;
        this.category = category;
        this.user = user;
        this.location = location;
        this.description = description;
        this.media = media;
        this.category = category;
    }

    //getters

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

    //setters


    public void setReportID(int reportID) {
        this.reportID = reportID;
    }

    public void setUser(User user) {
        this.user = user;
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
}
