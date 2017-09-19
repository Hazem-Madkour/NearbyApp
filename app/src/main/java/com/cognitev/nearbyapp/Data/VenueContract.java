package com.cognitev.nearbyapp.Data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class VenueContract {
    public static final String CONTENT_AUTHORITY = "com.cognitev.nearbyapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIES = "venues";

    public static final class VenuesColumns implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();
        public static final String TABLE_NAME = "venue";
        public static final String COLUMN_KEY = "venue_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_FORMATTED_ADDRESS = "formatted_address";
        public static final String COLUMN_SUFFIX = "suffix";
        public static final String COLUMN_PREFIX = "prefix";
        public static final String COLUMN_LAT = "lat";
        public static final String COLUMN_LNG = "lng";

        public static Uri getVenueUri() {
            return CONTENT_URI.buildUpon().build();
        }

        public static Uri getVenueUri(long _id) {
            return ContentUris.withAppendedId(CONTENT_URI, _id);
        }
    }
}
