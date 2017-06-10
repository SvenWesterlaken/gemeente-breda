package com.svenwesterlaken.gemeentebreda.logic.util;

import com.svenwesterlaken.gemeentebreda.domain.Location;
import com.svenwesterlaken.gemeentebreda.domain.Report;
import com.svenwesterlaken.gemeentebreda.domain.User;

import java.util.HashMap;

/**
 * Created by Sven Westerlaken on 9-6-2017.
 */

public class ApiUtil {

    private static String BASE_URL = "http://37.34.59.50/breda";
    private static String REPORT_URL = "/CitySDK/services.json";
    private static String REPORT_REQUEST_URL = "/CitySDK/requests.json";
    private static String REPORT_GET =  "/?service_code=OV";
    private static String REPORT_UPVOTE_URL = "/CitySDK/upvoteRequest.json";

    private ApiUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static String getReportRequestURL() {
        return BASE_URL + REPORT_REQUEST_URL + REPORT_GET;
    }

    public static String getCategoryRequestURL() {
        return BASE_URL + REPORT_URL;
    }

    public static String getUpvoteRequestURL(int id) {
        return BASE_URL + REPORT_UPVOTE_URL + "?" + getReportIDParamater(id) +
                "&extraDescription=upvote";
    }

    public static String createReportAddURL(Report report) {
        return BASE_URL + REPORT_REQUEST_URL + "?" +
                getCategoryParameter() +
                getDescriptionParameter(report.getDescription()) +
                getLatLngParameter(report.getLocation()) +
                getStreetParameter(report.getLocation()) +
                getLocationIDParameter() +
                getUserParameters(report.getUser());

    }

    public static HashMap<String, String> getPostHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Cache-Control", "no-cache");
        headers.put("Content-Type", "application/json; charset=utf-8");
        headers.put("Server", "Microsoft-ISS/8.5");

        return headers;
    }

    //TODO: Needs to get a Category passed in for later use
    private static String getCategoryParameter() {
        return "service_code=OV";
    }

    private static String getDescriptionParameter(String d) {
        return "&description=" + replaceSpaces(d);
    }

    private static String getLatLngParameter(Location l) {
        return "&lat=" + l.getLatitude() + "&long=" + l.getLongitude();
    }

    private static String getStreetParameter(Location l) {
        return "&address_string=" + replaceSpaces(l.getStreet());
    }

    //TODO: Needs to get a location ID passed in for later use
    private static String getLocationIDParameter() {
        return "&address_id=1";
    }

    private static String getUserParameters(User u) {
        String[] name = u.getName().split(" ");
        String firstName = name[0];
        String lastName = name[1];

        String query = "&first_name=" + firstName + "&last_name=" + lastName;

        if (u.getMobileNumber() != null){
            query = query + "&phone=" + u.getMobileNumber();
        }

        return query;
    }

    private static String getReportIDParamater(int id) {
        return "service_request_id=" + id;
    }

    private static String replaceSpaces(String str){
        return str.replaceAll(" ", "%20");
    }

}
