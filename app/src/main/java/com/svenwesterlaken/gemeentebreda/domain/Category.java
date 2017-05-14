package com.svenwesterlaken.gemeentebreda.domain;

/**
 * Created by lukab on 9-5-2017.
 */

public class Category {
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
}
