package com.svenwesterlaken.gemeentebreda.domain;

import android.icu.util.ULocale;
import android.os.Parcelable;

import org.parceler.Parcel;

import java.io.Serializable;

/**
 * Created by lukab on 9-5-2017.
 */

public class Report implements Parcelable {

    int reportID, locationID, categoryID, userID;
    User user;
    Location location;
    String description;
    Media media;
    Category category;
    String status;

    public Report() {
    }

    public Report(int reportID, User user, Location location, String description, Category category, int locationID, String status) {
        this.reportID = reportID;
        this.category = category;
        this.user = user;
        this.location = location;
        this.description = description;
        this.category = category;
        this.locationID = locationID;
        this.status = status;
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
    
    public String getStatus() { return status; }

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
    
    public void setStatus(String status) { this.status = status; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(this.reportID);
        dest.writeInt(this.locationID);
        dest.writeInt(this.categoryID);
        dest.writeInt(this.userID);
        dest.writeParcelable(this.user, flags);
        dest.writeParcelable(this.location, flags);
        dest.writeString(this.description);
        dest.writeParcelable(this.media, flags);
        dest.writeParcelable(this.category, flags);
        dest.writeString(this.status);
    }

    protected Report(android.os.Parcel in) {
        this.reportID = in.readInt();
        this.locationID = in.readInt();
        this.categoryID = in.readInt();
        this.userID = in.readInt();
        this.user = in.readParcelable(User.class.getClassLoader());
        this.location = in.readParcelable(Location.class.getClassLoader());
        this.description = in.readString();
        this.media = in.readParcelable(Media.class.getClassLoader());
        this.category = in.readParcelable(Category.class.getClassLoader());
        this.status = in.readString();
    }

    public static final Parcelable.Creator<Report> CREATOR = new Parcelable.Creator<Report>() {
        @Override
        public Report createFromParcel(android.os.Parcel source) {
            return new Report(source);
        }

        @Override
        public Report[] newArray(int size) {
            return new Report[size];
        }
    };
}
