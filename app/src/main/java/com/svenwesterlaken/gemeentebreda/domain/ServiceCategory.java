package com.svenwesterlaken.gemeentebreda.domain;
import com.google.gson.annotations.SerializedName;

/**
 * Created by lukab on 2-6-2017.
 */

public class ServiceCategory {
    @SerializedName("service_name")
    public String categoryName;
    @SerializedName("description")
    public String description;
    @SerializedName("service_code")
    public String categoryCode;
    @SerializedName("group")
    public String filter;

    public ServiceCategory(String categoryName, String description, String categoryCode, String filter) {
        this.categoryName = categoryName;
        this.description = description;
        this.categoryCode = categoryCode;
        this.filter = filter;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getDescription() {
        return description;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public String getFilter() {
        return filter;
    }

    //http://37.34.59.50/breda/CitySDK/services.json

}
