package com.svenwesterlaken.gemeentebreda.data.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;


/**
 * Created by lukab on 9-5-2017.
 */

public class ReportDatabase extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "reports.db"; //aanpassen als andere db;
    private static final int DATABASE_VERSION = 1;

    public ReportDatabase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public Cursor getReports(){
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT rowid as _id, description FROM reports"; //aanpassen als db klaar is
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        db.close();;
        return c;
    }
}
