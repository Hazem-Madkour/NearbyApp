package com.cognitev.nearbyapp.Utilities;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.CursorLoader;

import com.cognitev.nearbyapp.Data.VenueContract.VenuesColumns;
import com.cognitev.nearbyapp.Models.Venue;

import java.util.ArrayList;

public class UtilityDatabase {

    public static final String[] VENUE_COLUMNS = {
            VenuesColumns.TABLE_NAME + "." + VenuesColumns._ID,
            VenuesColumns.COLUMN_KEY,
            VenuesColumns.COLUMN_NAME,
            VenuesColumns.COLUMN_FORMATTED_ADDRESS,
            VenuesColumns.COLUMN_SUFFIX,
            VenuesColumns.COLUMN_PREFIX,
            VenuesColumns.COLUMN_LAT,
            VenuesColumns.COLUMN_LNG
    };
    public static final int COL_VENUE_KEY = 1;
    public static final int COL_VENUE_NAME = 2;
    public static final int COL_VENUE_FORMATTED_ADDRESS = 3;
    public static final int COL_VENUE_SUFFIX = 4;
    public static final int COL_VENUE_PREFIX = 5;
    public static final int COL_VENUE_LAT = 6;
    public static final int COL_VENUE_LNG = 7;

    static public void insertVenues(Context context, ArrayList<Venue> lstVenues) {
        if (context == null) return;
        clearDatabase(context);
        if (lstVenues == null || lstVenues.size() == 0) return;
        context.getContentResolver().bulkInsert(VenuesColumns.CONTENT_URI,
                UtilityConversions.convertFromVenuesToContentValues(lstVenues));
    }

    static public boolean isVenueExists(Context context) {
        Cursor venueCursor = context.getContentResolver().query(
                VenuesColumns.CONTENT_URI,
                null,
                null,
                null,
                null);
        boolean bExist = venueCursor != null && venueCursor.moveToFirst();
        if (venueCursor != null) {
            venueCursor.close();
        }
        return bExist;
    }

    static public CursorLoader getVenuesCursorLoader(Context context) {
        Uri VenueUri = VenuesColumns.getVenueUri();
        return new CursorLoader(context,
                VenueUri,
                VENUE_COLUMNS,
                null,
                null,
                null);
    }

    static public void clearDatabase(Context context) {
        context.getContentResolver().delete(VenuesColumns.CONTENT_URI, null, null);
    }

}
