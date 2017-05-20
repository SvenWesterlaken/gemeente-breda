package com.svenwesterlaken.gemeentebreda.domain;

import java.io.Serializable;

/**
 * Created by lukab on 9-5-2017.
 */

public class Location implements Serializable{
    String street, city, postalCode;
    int houseNumber,  locationID;
    Double latitude, longitude;

    public Location(String street, String city, int houseNumber, String postalCode, int locationID, Double latitude, Double longitude ) {
        this.street = street;
        this.locationID = locationID;
        this.city = city;
        this.houseNumber = houseNumber;
        this.postalCode = postalCode;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Location(){};

    //getters


    public String getStreet() {
        return street;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCity() {
        return city;
    }

    public int getHouseNumber() {
        return houseNumber;
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

    public void setCity(String city) {
        this.city = city;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setHouseNumber(int houseNumber) {
        this.houseNumber = houseNumber;
    }

    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public void setLocationID(int locationID) {
        this.locationID = locationID;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
}
