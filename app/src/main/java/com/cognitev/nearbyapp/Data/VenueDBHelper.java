package com.cognitev.nearbyapp.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cognitev.nearbyapp.Data.VenueContract.VenuesColumns;

public class VenueDBHelper extends SQLiteOpenHelper {
    static final String DATABASE_NAME = "venue.db";
    private static final int DATABASE_VERSION = 1;

    public VenueDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_VENUES_TABLE = "CREATE TABLE " + VenuesColumns.TABLE_NAME + " (" +
                VenuesColumns._ID + " INTEGER PRIMARY KEY," +
                VenuesColumns.COLUMN_KEY + " TEXT NOT NULL, " +
                VenuesColumns.COLUMN_NAME + " TEXT NOT NULL, " +
                VenuesColumns.COLUMN_FORMATTED_ADDRESS + " TEXT , " +
                VenuesColumns.COLUMN_SUFFIX + " TEXT , " +
                VenuesColumns.COLUMN_PREFIX + " TEXT , " +
                VenuesColumns.COLUMN_LAT + " REAL NOT NULL ," +
                VenuesColumns.COLUMN_LNG + " REAL NOT NULL " +
                " );";
        sqLiteDatabase.execSQL(SQL_CREATE_VENUES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        onCreate(sqLiteDatabase);
    }
}
