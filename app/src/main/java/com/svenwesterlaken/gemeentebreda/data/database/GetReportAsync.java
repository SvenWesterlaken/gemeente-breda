package com.svenwesterlaken.gemeentebreda.data.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.svenwesterlaken.gemeentebreda.domain.Report;

/**
 * Created by Sven Westerlaken on 12-6-2017.
 */

public class GetReportAsync extends AsyncTask<String, Void, Cursor> {
    private ReportListener listener;
    private DatabaseHandler handler;
    private SQLiteDatabase db;

    public GetReportAsync(ReportListener listener, DatabaseHandler handler) {
        this.listener = listener;
        this.handler = handler;
    }

    @Override
    protected Cursor doInBackground(String... params) {
        String query = null;

        if(params != null) {
            query = params[0];
        }

        db = handler.getReadableDatabase();

        return db.rawQuery(query, null);
    }

    protected void onPostExecute(Cursor cursor) {

        while(cursor.moveToNext() ) {

            Report report = new Report();

            report.setCategory(handler.getCategory(cursor.getInt(cursor.getColumnIndex("categoryId"))));

            report.setReportID( cursor.getInt(cursor.getColumnIndex("reportId")));
            report.setDescription( cursor.getString(cursor.getColumnIndex("description")));
            report.setLocation(handler.getLocation(cursor.getInt(cursor.getColumnIndex("locationId"))));
            report.setUpvotes( cursor.getInt(cursor.getColumnIndex("upvotes")));
            report.setStatus( cursor.getString(cursor.getColumnIndex("status")));


            listener.onReportsAvailable(report);
        }

        cursor.close();
        db.close();
        listener.onFinished();


    }

    public interface ReportListener {
        void onReportsAvailable(Report report);
        void onFinished();
    }
}
