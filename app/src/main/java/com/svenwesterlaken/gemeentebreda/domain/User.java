package com.svenwesterlaken.gemeentebreda.domain;

import java.io.Serializable;

/**
 * Created by lukab on 9-5-2017.
 */

public class User implements Serializable{
    int userID;
    String name, emailaddress, mobileNumber;


    public User() {
    }

    public User(int userID, String mobileNumber, String name, String emailaddress ) {
        this.emailaddress = emailaddress;
        this.mobileNumber = mobileNumber;
        this.name = name;
        this.userID = userID;
    }

    //getters


    public int getUserID() {
        return userID;
    }

    public String getMobileNumber() {
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

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public void setEmailaddress(String emailaddress) {
        this.emailaddress = emailaddress;
    }

    public void setName(String name) {
        this.name = name;
    }
}
