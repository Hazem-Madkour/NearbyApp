package com.cognitev.nearbyapp.Utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;

public class Storage {
    //region Keys
    public static final String APP_STATUS_KEY = "appstatuskey";
    public static final String LAST_LOCATION_KEY = "lastlocationkey";
    private static final String DEFAULT_STRING_LOAD = "z#xx@aq1";
    private static Storage instance;
    //endregion
    //region SharedPreferences Fields
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mSharedPreferences;
    //endregion

    //region Constructor
    public Storage(Context context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mEditor = mSharedPreferences.edit();
    }
    //endregion

    public static Storage getInstance(Context context) {
        if (instance == null)
            instance = new Storage(context);
        return instance;
    }

    //region Last Location
    public void saveLastLocation(Location location) {
        try {
            mEditor.putString(LAST_LOCATION_KEY, String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude()));
            mEditor.commit();
        } catch (Exception ex) {
        }
    }

    public Location loadLastLocation() {
        String temp = mSharedPreferences.getString(LAST_LOCATION_KEY, DEFAULT_STRING_LOAD);
        if (temp.equals(DEFAULT_STRING_LOAD)) return null;
        else {
            try {
                Location location = new Location("Location");
                location.setLatitude(Double.parseDouble(temp.split(",")[0]));
                location.setLongitude(Double.parseDouble(temp.split(",")[1]));
                return location;
            } catch (Exception e) {
                return null;
            }
        }
    }

    public boolean isAcceptableChangeWithSave(Location location) {
        Location oldLocation = loadLastLocation();
        if (oldLocation == null || UtilityGeneral.calculateDistanceInMeters(oldLocation, location) >= UtilityGeneral.LOCATION_LISTENER_MIN_DISTANCE) {
            saveLastLocation(location);
            return true;
        } else
            return false;
    }

    public boolean isLocationExist() {
        return loadLastLocation() != null;
    }
    //endregion

    //region AppStatus
    public boolean changeAppStatus() {
        boolean realTime = !isRealTime();
        mEditor.putBoolean(APP_STATUS_KEY, realTime);
        mEditor.commit();
        return realTime;
    }

    public boolean isRealTime() {
        return mSharedPreferences.getBoolean(APP_STATUS_KEY, true);
    }
    //endregion
}
