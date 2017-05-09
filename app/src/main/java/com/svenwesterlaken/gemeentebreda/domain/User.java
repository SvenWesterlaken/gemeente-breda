package com.svenwesterlaken.gemeentebreda.domain;

/**
 * Created by lukab on 9-5-2017.
 */

public class User {
    int userID, mobileNumber;
    String name, emailaddress;

    public User(int userID, int mobileNumber, String name, String emailaddress ) {
        this.emailaddress = emailaddress;
        this.mobileNumber = mobileNumber;
        this.name = name;
        this.userID = userID;
    }

    //getters


    public int getUserID() {
        return userID;
    }

    public int getMobileNumber() {
        return mobileNumber;
    }

    public String getName() {
        return name;
    }

    public String getEmailaddress() {
        return emailaddress;
    }

    //setters


    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setMobileNumber(int mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public void setEmailaddress(String emailaddress) {
        this.emailaddress = emailaddress;
    }

    public void setName(String name) {
        this.name = name;
    }
}
