package com.cognitev.nearbyapp.Data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class VenueProvider extends ContentProvider {

    private VenueDBHelper mVenueDBHelper;

    @Override
    public boolean onCreate() {
        mVenueDBHelper = new VenueDBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        retCursor = mVenueDBHelper.getReadableDatabase().query(
                VenueContract.VenuesColumns.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final SQLiteDatabase db = mVenueDBHelper.getWritableDatabase();
        Uri returnUri;
        long _id = db.insert(VenueContract.VenuesColumns.TABLE_NAME, null, contentValues);
        if (_id > 0)
            returnUri = VenueContract.VenuesColumns.getVenueUri(_id);
        else
            throw new android.database.SQLException("Failed to insert row into " + uri);
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mVenueDBHelper.getWritableDatabase();
        int rowsDeleted;
        rowsDeleted = db.delete(
                VenueContract.VenuesColumns.TABLE_NAME, selection, selectionArgs);
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mVenueDBHelper.getWritableDatabase();
        int rowsUpdated = db.update(VenueContract.VenuesColumns.TABLE_NAME, contentValues, selection,
                selectionArgs);
        if (rowsUpdated != 0)
            getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }


    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mVenueDBHelper.getWritableDatabase();

        int numRowsInsert = 0;
        db.beginTransaction();
        for (ContentValues value : values) {
            long _id = db.insert(VenueContract.VenuesColumns.TABLE_NAME, null, value);
            if (_id != -1) {
                numRowsInsert++;
            }
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        getContext().getContentResolver().notifyChange(uri, null);
        return numRowsInsert;
    }


    @Override
    public void shutdown() {
        mVenueDBHelper.close();
        super.shutdown();
    }
}
