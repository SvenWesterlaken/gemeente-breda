package com.svenwesterlaken.gemeentebreda.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by lukab on 9-5-2017.
 */

public class Category implements Parcelable {
    int categoryID;
    String categoryName, categorySummary;

    public Category(int categoryID, String categoryName, String categorySummary) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
        this.categorySummary = categorySummary;
    }

    public Category(){

    }

    //getters

    public int getCategoryID() {
        return categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getCategorySummary() {
        return categorySummary;
    }

    //setters


    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setCategorySummary(String categorySummary) {
        this.categorySummary = categorySummary;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.categoryID);
        dest.writeString(this.categoryName);
        dest.writeString(this.categorySummary);
    }

    protected Category(Parcel in) {
        this.categoryID = in.readInt();
        this.categoryName = in.readString();
        this.categorySummary = in.readString();
    }

    public static final Parcelable.Creator<Category> CREATOR = new Parcelable.Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel source) {
            return new Category(source);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };
}
