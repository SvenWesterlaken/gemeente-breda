package com.svenwesterlaken.gemeentebreda.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lukab on 9-5-2017.
 */

public class User implements Parcelable {
    int userID;
    String name;
    String emailaddress;
    String mobileNumber;


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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.userID);
        dest.writeString(this.name);
        dest.writeString(this.emailaddress);
        dest.writeString(this.mobileNumber);
    }

    protected User(Parcel in) {
        this.userID = in.readInt();
        this.name = in.readString();
        this.emailaddress = in.readString();
        this.mobileNumber = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
