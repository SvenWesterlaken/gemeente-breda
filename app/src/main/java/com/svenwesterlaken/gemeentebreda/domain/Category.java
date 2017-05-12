package com.svenwesterlaken.gemeentebreda.domain;

/**
 * Created by lukab on 9-5-2017.
 */

public class Category {
    int categoryID;
    String categoryName;

    public Category(int categoryID, String categoryName) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
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

    //setters


    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
