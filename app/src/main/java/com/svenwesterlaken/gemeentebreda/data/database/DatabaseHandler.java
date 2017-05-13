package com.svenwesterlaken.gemeentebreda.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.svenwesterlaken.gemeentebreda.domain.Category;
import com.svenwesterlaken.gemeentebreda.domain.Location;
import com.svenwesterlaken.gemeentebreda.domain.Media;
import com.svenwesterlaken.gemeentebreda.domain.Report;
import com.svenwesterlaken.gemeentebreda.domain.User;

import java.util.ArrayList;

/**
 * Created by Whrabbit on 5/9/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper{
    //CLASSES
    private static final String TAG = "GemeenteDBHandler";

    private static final int DB_VERSION = 5;
    private static final String DB_NAME = "gemeente.db";

    private static final String USER_TABLE_NAME = "user";
        private static final String USER_COLUMN_ID = "userId";
        private static final String USER_COLUMN_NAME = "name";
        private static final String USER_COLUMN_PHONE = "phone";
        private static final String USER_COLUMN_EMAIL = "email";

    private static final String REPORT_TABLE_NAME = "report";
        private static final String REPORT_COLUMN_ID = "reportId";
        private static final String REPORT_COLUMN_CATEGORYID = "categoryId";
        private static final String REPORT_COLUMN_DESCRIPTION = "description";
        private static final String REPORT_COLUMN_MEDIAID = "mediaId";
        private static final String REPORT_COLUMN_LOCATIONID = "locationId";


    private static final String USER_REPORT_TABLE_NAME = "user_report";
        private static final String USER_REPORT_COLUMN_USERID = "userId";
        private static final String USER_REPORT_COLUMN_REPORTID = "reportId";

    private static final String LOCATION_TABLE_NAME = "location";
        private static final String LOCATION_COLUMN_ID = "locationId";
        private static final String LOCATION_COLUMN_LATITUDE = "latitude";
        private static final String LOCATION_COLUMN_LONGITUDE = "longitude";
        private static final String LOCATION_COLUMN_STREET = "steet";
        private static final String LOCATION_COLUMN_STREETNR = "streetnr";
        private static final String LOCATION_COLUMN_POSTALCODE = "postalcode";
        private static final String LOCATION_COLUMN_CITY = "city";

    private static final String CATEGORY_TABLE_NAME = "category";
        private static final String CATEGORY_COLUMN_ID = "categoryId";
        private static final String CATEGORY_COLUMN_NAME = "categoryName";
        private static final String CATEGORY_COLUMN_SUMMARY = "summary";

    private static final String MEDIA_TABLE_NAME = "media";
        private static final String MEDIA_COLUMN_ID = "mediaId";

    private static final String VIDEO_TABLE_NAME = "video";
        private static final String VIDEO_COLUMN_ID = "mediaId";
        private static final String VIDEO_COLUMN_LENGTH = "length";

    private static final String PHOTO_TABLE_NAME = "photo";
        private static final String PHOTO_COLUMN_ID = "mediaId";

    public DatabaseHandler (Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, DB_NAME, factory, DB_VERSION);

        testData();

    }
    //CREATE DATABASE WITH SQL
    public void onCreate(SQLiteDatabase db){
        Log.i(TAG, "creating database");
        String CREATE_USER_TABLE = "CREATE TABLE " + USER_TABLE_NAME + "(" +
                USER_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                USER_COLUMN_NAME + " TEXT," +
                USER_COLUMN_PHONE + " TEXT," +
                USER_COLUMN_EMAIL + " TEXT" +
                ");";

        String CREATE_LOCATION_TABLE = "CREATE TABLE " + LOCATION_TABLE_NAME + "(" +
                LOCATION_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                LOCATION_COLUMN_LATITUDE + " DECIMAL(5,5)," +
                LOCATION_COLUMN_LONGITUDE + " DECIMAL(5,5)," +
                LOCATION_COLUMN_STREET + " TEXT," +
                LOCATION_COLUMN_STREETNR + " TEXT," +
                LOCATION_COLUMN_POSTALCODE + " TEXT," +
                LOCATION_COLUMN_CITY + " TEXT" +
                ");";

        String CREATE_CATEGORY_TABLE = "CREATE TABLE " + CATEGORY_TABLE_NAME + "(" +
                CATEGORY_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                CATEGORY_COLUMN_NAME + " TEXT, " +
                CATEGORY_COLUMN_SUMMARY + " TEXT " +
                ");";

        String CREATE_MEDIA_TABLE = "CREATE TABLE " + MEDIA_TABLE_NAME +  "(" +
                MEDIA_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" +
                ");";

        String CREATE_VIDEO_TABLE = "CREATE TABLE " + VIDEO_TABLE_NAME + "(" +
                VIDEO_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                VIDEO_COLUMN_LENGTH + " TEXT, " +

                "FOREIGN KEY (" + VIDEO_COLUMN_ID + ")" +
                "REFERENCES " + MEDIA_TABLE_NAME +  "(" + MEDIA_COLUMN_ID + ")" +

                ");";

        String CREATE_PHOTO_TABLE = "CREATE TABLE " + PHOTO_TABLE_NAME + "(" +
                PHOTO_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                "FOREIGN KEY (" + PHOTO_COLUMN_ID + ")" +
                "REFERENCES " + MEDIA_TABLE_NAME +  "(" + MEDIA_COLUMN_ID + ")" +

                ");";

        String CREATE_REPORT_TABLE = "CREATE TABLE " + REPORT_TABLE_NAME + "(" +
                REPORT_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                REPORT_COLUMN_CATEGORYID + " INTEGER, " +
                REPORT_COLUMN_DESCRIPTION + " TEXT," +
                REPORT_COLUMN_MEDIAID + " INTEGER," +
                REPORT_COLUMN_LOCATIONID + " INTEGER," +

                "FOREIGN KEY (" + REPORT_COLUMN_CATEGORYID + ")" +
                "REFERENCES " + CATEGORY_TABLE_NAME + "(" + CATEGORY_COLUMN_ID + ")," +

                "FOREIGN KEY (" + REPORT_COLUMN_LOCATIONID + ")" +
                "REFERENCES " + REPORT_TABLE_NAME +  "(" + REPORT_COLUMN_ID + ")," +

                "FOREIGN KEY (" + REPORT_COLUMN_MEDIAID + ")" +
                "REFERENCES " + MEDIA_TABLE_NAME +  "(" + MEDIA_COLUMN_ID + ")" +

                ");";
        String CREATE_USER_REPORT_TABLE = "CREATE TABLE " + USER_REPORT_TABLE_NAME + "(" +
                USER_REPORT_COLUMN_USERID + " INTEGER, " +
                USER_REPORT_COLUMN_REPORTID + " INTEGER, " +

                "FOREIGN KEY (" + USER_REPORT_COLUMN_USERID + ")" +
                "REFERENCES " + USER_TABLE_NAME + "(" + USER_COLUMN_ID + ")," +

                "FOREIGN KEY (" + USER_REPORT_COLUMN_REPORTID + ")" +
                "REFERENCES " + REPORT_TABLE_NAME +  "(" + REPORT_COLUMN_ID + ")" +
                ");";

        db.execSQL("PRAGMA foreign_keys = ON");
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_REPORT_TABLE);
        db.execSQL(CREATE_USER_REPORT_TABLE);
        db.execSQL(CREATE_LOCATION_TABLE);
        db.execSQL(CREATE_CATEGORY_TABLE);
        db.execSQL(CREATE_MEDIA_TABLE);
        db.execSQL(CREATE_VIDEO_TABLE);
        db.execSQL(CREATE_PHOTO_TABLE);

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void testData(){
        Category category1 = new Category(1, "Straatverlichting");
        addCategory(category1);
        User user1 = new User(1, "0642584793", "gebruiker1", "gebruiker1@gmail.com");
        addUser(user1);
        Location location1 = new Location("Rijckevorsel", "Breda", 54, "2991kc", 1, "iets", "iets");
        addLocation(location1);

        addReport(new Report(1, user1, location1, "Eerste test", category1));

        Category category2 = new Category(1, "Vuilnis");
        addCategory(category2);
        User user2 = new User(1, "0642584423", "Gebruiker2", "gebruiker2@gmail.com");
        addUser(user2);
        Location location2 = new Location("Vrijenburg", "Breda", 54, "4815kc", 2, "iets", "iets");
        addLocation(location2);

        addReport(new Report(2, user2, location2, "Tweede test", category2));
    }
    //ADD USERS
    public void addUser(User user){
        ContentValues values = new ContentValues();
        values.put(USER_COLUMN_NAME, user.getName());
//        values.put(USER_COLUMN_PHONE, user.getPhone());
//        values.put(USER_COLUMN_EMAIL, user.getEmail());
        values.put(USER_COLUMN_PHONE, user.getMobileNumber());
        values.put(USER_COLUMN_EMAIL, user.getEmailaddress());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(USER_TABLE_NAME, null, values);
        Log.i("TAG", "added user");
        db.close();
    }

    //ADD REPORTS
    public void addReport(Report report){
        ContentValues values = new ContentValues();

        values.put(REPORT_COLUMN_CATEGORYID, report.getCategory().getCategoryID());
        values.put(REPORT_COLUMN_DESCRIPTION, report.getDescription());
//        values.put(REPORT_COLUMN_MEDIAID, report.getMedia().getMediaID());
        values.put(REPORT_COLUMN_LOCATIONID, report.getLocation().getLocationID());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(REPORT_TABLE_NAME, null, values);
        Log.i("TAG", "added report");
        db.close();
    }

    //ADD LOCATIONS
    public void addLocation(Location location){
        ContentValues values = new ContentValues();

        values.put(LOCATION_COLUMN_LATITUDE, location.getLattitude());
        values.put(LOCATION_COLUMN_LONGITUDE, location.getLongitude());
        values.put(LOCATION_COLUMN_STREET, location.getStreet());
        values.put(LOCATION_COLUMN_STREETNR, location.getHouseNumber());
        values.put(LOCATION_COLUMN_CITY, location.getCity());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(LOCATION_TABLE_NAME, null, values);
        Log.i("TAG", "added location");
        db.close();
    }



    //ADD CATEGORIES
    public void addCategory(Category category){
        ContentValues values = new ContentValues();
        values.put(CATEGORY_COLUMN_NAME, category.getCategoryName());


        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(CATEGORY_TABLE_NAME, null, values);
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

        String query = "SELECT " + USER_COLUMN_NAME + " FROM " + USER_TABLE_NAME + " WHERE " +
                USER_COLUMN_ID + "=" + userID;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        //cursor.moveToFirst();
        Log.i("TAG", "before while");

        while(cursor.moveToNext() ) {
            user = new User();
            user.setName(cursor.getString(cursor.getColumnIndex(USER_COLUMN_NAME)));
            Log.i("TAG", "got user");
        };

        db.close();

        return user;
    }

    public Location getLocation(int locationID){
        Location location = null;

        String query = "SELECT * FROM " + LOCATION_TABLE_NAME + " WHERE " +
                LOCATION_COLUMN_ID + "="  + locationID + ";";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        //cursor.moveToFirst();
        Log.i("TAG", "before while");

        while(cursor.moveToNext() ) {
            location = new Location();
            location.setCity(cursor.getString(cursor.getColumnIndex(LOCATION_COLUMN_CITY)));
            location.setStreet(cursor.getString(cursor.getColumnIndex(LOCATION_COLUMN_STREET)));
            location.setHouseNumber(cursor.getInt(cursor.getColumnIndex(LOCATION_COLUMN_STREETNR)));
            Log.i("TAG", "got location");
        };

        db.close();

        return location;
    }



    //ARRAYLIST FOR REPORTS
    public ArrayList<Report> getReport(String name){
        ArrayList<Report> reports = new ArrayList<>();

        String query = "SELECT * FROM " + REPORT_TABLE_NAME + " WHERE " +
                USER_REPORT_COLUMN_REPORTID + "=" + "\"" + name + "\"";

        String queryALL = "SELECT * FROM " + REPORT_TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        while(cursor.moveToNext() ) {

            Report report = new Report();

            report.setReportID(cursor.getInt(cursor.getColumnIndex(REPORT_COLUMN_ID)));
            report.setCategoryID(cursor.getInt(cursor.getColumnIndex(REPORT_COLUMN_CATEGORYID)));
            report.setDescription(cursor.getString(cursor.getColumnIndex(REPORT_COLUMN_DESCRIPTION)));
//            report.setMediaId(cursor.getInt(cursor.getColumnIndex(REPORT_COLUMN_MEDIAID)));
            report.setLocationID(cursor.getInt(cursor.getColumnIndex(REPORT_COLUMN_LOCATIONID)));
//            report.setUserID(cursor.getInt(cursor.getColumnIndex(REPORT_COLUMN_USERID)));

            reports.add(report);
        }

        db.close();



        return reports;
    }

    public Category getCategory(int reportID){

        Category category = null;
        String query = "SELECT * FROM " + CATEGORY_TABLE_NAME + " WHERE " + CATEGORY_COLUMN_ID + " = " + reportID;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        while(cursor.moveToNext()){
            category = new Category();
            category.setCategoryID(cursor.getInt(cursor.getColumnIndex(CATEGORY_COLUMN_ID)));
            category.setCategoryName(cursor.getString(cursor.getColumnIndex(CATEGORY_COLUMN_NAME)));

        }

        return category;
    }

    //ARRAYLIST FOR REPORTS
    public ArrayList<Report> getAllReports(){
        ArrayList<Report> reports = new ArrayList<>();

        String query = "SELECT * FROM " + REPORT_TABLE_NAME + ";";


        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        cursor.moveToFirst();

        while(cursor.moveToNext() ) {

            Report report = new Report();

            report.setCategory(getCategory(cursor.getInt(cursor.getColumnIndex(REPORT_COLUMN_ID))));

            report.setReportID( cursor.getInt(cursor.getColumnIndex(REPORT_COLUMN_ID)));

            report.setDescription( cursor.getString(cursor.getColumnIndex(REPORT_COLUMN_DESCRIPTION)));
//            report.setMediaId(cursor.getInt(cursor.getColumnIndex(REPORT_COLUMN_MEDIAID)));
            report.setLocation( getLocation(cursor.getInt(cursor.getColumnIndex(REPORT_COLUMN_LOCATIONID))));
            //report.setUser( getUser(cursor.getInt(cursor.getColumnIndex(REPORT_COLUMN_USERID))));



            reports.add(report);
        }

        cursor.close();

        return reports;

    }



}
