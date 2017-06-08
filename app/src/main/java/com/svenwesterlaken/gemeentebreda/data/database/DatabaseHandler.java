package com.svenwesterlaken.gemeentebreda.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;
import com.svenwesterlaken.gemeentebreda.domain.Category;
import com.svenwesterlaken.gemeentebreda.domain.Location;
import com.svenwesterlaken.gemeentebreda.domain.Media;
import com.svenwesterlaken.gemeentebreda.domain.Report;
import com.svenwesterlaken.gemeentebreda.domain.User;

import java.util.ArrayList;

import static android.R.attr.id;

/**
 * Created by Luka Brinkman
 */

public class DatabaseHandler extends SQLiteAssetHelper {
    //CLASSES
    private static final String DB_NAME = "gemeenteDB.sqlite";
    private static final int DB_VERSION = 5;


    public DatabaseHandler (Context context){
        super(context, DB_NAME, null, DB_VERSION);


    }


    //ADD USERS
    public void addUser(User user){

        String name = user.getName();
        String phone = user.getMobileNumber();
        String email = user.getEmailaddress();
        int userId = user.getUserID();

        SQLiteDatabase db = this.getWritableDatabase();

        String query = "INSERT INTO user VALUES (" + userId +  " , '" + name + "' ,  '" + phone + "', '" + email + "');";
        db.execSQL(query);

        Log.i("TAG", "added user");
        db.close();
    }

    public void addFavourite(User user, Report report){

        int reportId = report.getReportID();
        int userId = user.getUserID();

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "INSERT INTO favourite(reportId, userId) VALUES (" + reportId +  " , " + userId  + " );";
        db.execSQL(query);
        Log.i("TAG", "added favourite");
        db.close();

    }

    //ADD REPORTS
    public void addReport(Report report){

        SQLiteDatabase db = this.getWritableDatabase();
        int reportId = report.getReportID();
        int locationId = report.getLocation().getLocationID();
        int categoryId = report.getCategory().getCategoryID();
        String description = report.getDescription();

        String query = "INSERT INTO report(reportId, locationId, categoryId, description) VALUES (" + reportId + " , " +
                locationId + " , " + categoryId + " , '" + description + "');";
        db.execSQL(query);
        db.close();
    }

    //ADD LOCATIONS
    public void addLocation(Location location){

        SQLiteDatabase db = this.getWritableDatabase();

        String query = "INSERT INTO location(locationId, latitude, longitude, street) VALUES (" +
                location.getLocationID() + " , " + location.getLatitude() + " , " + location.getLongitude() + " , '" +
                location.getStreet() + "');";
        db.execSQL(query);
        Log.i("TAG", "added location");
        db.close();
    }


    public void addReportUser(Report report){

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "INSERT INTO user_report VALUES (" + 1 + " , " + report.getReportID() + ");";
        db.execSQL(query);
        db.close();
    }

    public ArrayList<Report> getReportUser(User user) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM user_report WHERE userId = " + user.getUserID();
        Cursor cursor = db.rawQuery(query, null);

        //cursor.moveToFirst();
        Log.i("TAG", "before while");
        ArrayList<Report> reports = new ArrayList<Report>();

        while(cursor.moveToNext() ) {
            Report report = new Report();
            int reportId = cursor.getInt(cursor.getColumnIndex("reportId"));
            report = getReport(reportId);

            reports.add(report);
            Log.i("TAG", "got user");
        };

        cursor.close();
        db.close();

        return reports;
    }

    //ADD CATEGORIES
    public void addCategory(Category category){

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "INSERT INTO category(categoryId, categoryName, summary) VALUES (" + category.getCategoryID() + ", '" + category.getCategoryName() +
                "' , '" + category.getCategorySummary() +  "');";
        db.execSQL(query);
        db.close();
    }


//    //ADD MEDIA
//    public void addMedia(Media media){
//        ContentValues values = new ContentValues();
//        values.put(MEDIA_COLUMN_ID, media.getMediaId());
//
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.insert(MEDIA_TABLE_NAME, null, values);
//        db.close();
//    }
//    //ADD PHOTO
//    public void addPhoto(Photo photo){
//        ContentValues values = new ContentValues();
//        values.put(MEDIA_COLUMN_ID, photo.getMediaId());
//
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.insert(PHOTO_TABLE_NAME, null, values);
//        db.close();
//    }
//
//    //ADD VIDEO
//    public void addVideo(Video video){
//        ContentValues values = new ContentValues();
//        values.put(VIDEO_COLUMN_ID, video.getMediaId());
//
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.insert(VIDEO_TABLE_NAME, null, values);
//        db.close();
//    }

    //GET USERS
    public User getUser(int userID){
        User user = null;

        String query = "SELECT *" + " FROM user WHERE userId = " + userID;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        //cursor.moveToFirst();
        Log.i("TAG", "before while");

        while(cursor.moveToNext() ) {
            user = new User();
            user.setName(cursor.getString(cursor.getColumnIndex("name")));
            user.setUserID(1);
            user.setEmailaddress(cursor.getString(cursor.getColumnIndex("email")));
            user.setMobileNumber(cursor.getString(cursor.getColumnIndex("phone")));
        };

        cursor.close();
        db.close();

        return user;
    }

    public Location getLocation(int locationID){
        Location location = null;

        String query = "SELECT * FROM location WHERE locationId = "  + locationID + ";";
//

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        //cursor.moveToFirst();
        Log.i("TAG", "before while");

        while(cursor.moveToNext() ) {
            location = new Location();
            location.setLocationID(locationID);
            location.setStreet(cursor.getString(cursor.getColumnIndex("street")));
            location.setLatitude(cursor.getDouble(cursor.getColumnIndex("latitude")));
            location.setLongitude(cursor.getDouble(cursor.getColumnIndex("longitude")));
            Log.i("TAG", "got location " + cursor.getDouble(cursor.getColumnIndex("longitude")) + " " + cursor.getDouble(cursor.getColumnIndex("latitude")));
        };
        cursor.close();
        db.close();

        return location;
    }



    //ARRAYLIST FOR REPORTS
    public Report getReport(int id){


        Report report = new Report();

        String query = "SELECT * FROM Report" + " WHERE reportId = " + id + ";";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        while(cursor.moveToNext() ) {


            report.setReportID(cursor.getInt(cursor.getColumnIndex("reportId")));
            report.setDescription(cursor.getString(cursor.getColumnIndex("description")));
//            report.setMediaId(cursor.getInt(cursor.getColumnIndex(REPORT_COLUMN_MEDIAID)));
            report.setLocation(getLocation(cursor.getInt(cursor.getColumnIndex("locationId"))));
            report.setCategory(getCategory(cursor.getInt(cursor.getColumnIndex("categoryId"))));

        }

        cursor.close();
        db.close();

        return report;
    }

    public Category getCategory(int categoryID){

        Category category = null;
        String query = "SELECT * FROM  category WHERE categoryId = " + categoryID;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        while(cursor.moveToNext()){
            category = new Category();
            category.setCategoryID(cursor.getInt(cursor.getColumnIndex("categoryId")));
            category.setCategoryName(cursor.getString(cursor.getColumnIndex("categoryName")));
            category.setCategorySummary(cursor.getString(cursor.getColumnIndex("summary")));

        }

        cursor.close();

        return category;
    }

    //ARRAYLIST FOR REPORTS
    public ArrayList<Report> getAllReports(){
        ArrayList<Report> reports = new ArrayList<>();

        String query = "SELECT * FROM report ;";


        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);


        while(cursor.moveToNext() ) {

            Report report = new Report();

            report.setCategory(getCategory(cursor.getInt(cursor.getColumnIndex("categoryId"))));

            report.setReportID( cursor.getInt(cursor.getColumnIndex("reportId")));
            report.setDescription( cursor.getString(cursor.getColumnIndex("description")));
//            report.setMediaId(cursor.getInt(cursor.getColumnIndex(REPORT_COLUMN_MEDIAID)));
            report.setLocation( getLocation(cursor.getInt(cursor.getColumnIndex("locationId"))));




            reports.add(report);
        }

        cursor.close();
        return reports;

    }

    public ArrayList<Category> getAllCategories(){
        ArrayList<Category> categories  = new ArrayList<>();

        String query = "SELECT * FROM category ;";


        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);


        while(cursor.moveToNext() ) {

            Category category = new Category();

            category.setCategoryID(cursor.getInt(cursor.getColumnIndex("categoryId")));
            category.setCategoryName(cursor.getString(cursor.getColumnIndex("categoryName")));
            category.setCategorySummary(cursor.getString(cursor.getColumnIndex("summary")));



            categories.add(category);
        }

        cursor.close();
        return categories;

    }

    public ArrayList<Report> getFavourites(User user) {
        ArrayList<Report> reports = new ArrayList<>();

        String query = "SELECT * FROM favourite WHERE userId = " + user.getUserID() + " ;";


        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);


        while(cursor.moveToNext() ) {

            int reportId = cursor.getInt(cursor.getColumnIndex("reportId"));
            Report report = getReport(reportId);


            reports.add(report);
        }

        cursor.close();
        return reports;

    }

    public void deleteCategory(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM category";

        db.execSQL(query);
        db.close();
    }

    public void deleteFavourite(Report report){

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM favourite WHERE reportId  = " + report.getReportID();

        db.execSQL(query);

    }

    public  Report searchFavourite(Report report, User user) {

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM favourite  WHERE reportId = " + report.getReportID() + " AND userId = " + user.getUserID();
        Cursor cursor = db.rawQuery(query, null);
        Report report1 = null;

        while(cursor.moveToNext() ) {

            int reportId = cursor.getInt(cursor.getColumnIndex("reportId"));
            int userId = cursor.getInt(cursor.getColumnIndex("userId"));
            report1 = getReport(reportId);
        }

        cursor.close();

        return  report1;
    }
    
    public void deleteAllReports(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM Report";
        db.execSQL(query);
        
        db.close();
        
    }
    public void removeAllLocations(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM location";
        db.execSQL(query);
        
        db.close();
    }




}
