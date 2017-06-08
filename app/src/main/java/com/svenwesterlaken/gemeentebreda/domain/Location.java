package com.svenwesterlaken.gemeentebreda.domain;

import android.os.Parcel;
import android.os.Parcelable;

import org.parceler.ParcelClass;

import java.io.Serializable;

/**
 * Created by lukab on 9-5-2017.
 */

public class Location implements Parcelable {
    String street;
    int locationID;
    Double latitude, longitude;

    public Location(String street,int locationID, Double latitude, Double longitude ) {
        this.street = street;
        this.locationID = locationID;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Location(){};

    //getters


    public String getStreet() {
        return street;
    }

    public int getLocationID() {
        return locationID;
    }

    public Double getLongitude() { return longitude; }

    public Double getLatitude() {
        return latitude;
    }

    //setters


    public void setStreet(String street) {
        this.street = street;
    }
    

    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public void setLocationID(int locationID) {
        this.locationID = locationID;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.street);
        dest.writeInt(this.locationID);
        dest.writeValue(this.latitude);
        dest.writeValue(this.longitude);
    }

    protected Location(Parcel in) {
        this.street = in.readString();
        this.locationID = in.readInt();
        this.latitude = (Double) in.readValue(Double.class.getClassLoader());
        this.longitude = (Double) in.readValue(Double.class.getClassLoader());
    }

    public static final Parcelable.Creator<Location> CREATOR = new Parcelable.Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel source) {
            return new Location(source);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };
}
