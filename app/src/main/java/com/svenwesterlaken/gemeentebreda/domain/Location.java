package com.svenwesterlaken.gemeentebreda.domain;

/**
 * Created by lukab on 9-5-2017.
 */

public class Location {
    String street, city, postalCode;
    int houseNumber,  locationID;

    public Location(String street, String city, int houseNumber, String postalCode, int locationID ) {
        this.street = street;
        this.locationID = locationID;
        this.city = city;
        this.houseNumber = houseNumber;
        this.postalCode = postalCode;
    }

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

    public void setLocationID(int locationID) {
        this.locationID = locationID;
    }
}
