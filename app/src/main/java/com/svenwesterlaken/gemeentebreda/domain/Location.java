package com.svenwesterlaken.gemeentebreda.domain;

/**
 * Created by lukab on 9-5-2017.
 */

public class Location {
    String street, city, postalCode;
    int houseNumber,  locationID;
    String lattitude, longitude;

    public Location(String street, String city, int houseNumber, String postalCode, int locationID, String longitude, String lattitude ) {
        this.street = street;
        this.locationID = locationID;
        this.city = city;
        this.houseNumber = houseNumber;
        this.postalCode = postalCode;
        this.lattitude = lattitude;
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

    public String getLongitude() { return longitude; }

    public String getLattitude() {
        return lattitude;
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

    public void setLongitude(String longitude) { this.longitude = longitude; }

    public void setLocationID(int locationID) {
        this.locationID = locationID;
    }

    public void setLattitude(String lattitude) {
        this.lattitude = lattitude;
    }
}
