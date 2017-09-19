package com.cognitev.nearbyapp.Utilities;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.cognitev.nearbyapp.BuildConfig;
import com.cognitev.nearbyapp.Interfaces.IChangeLocationProviderChange;

public class LocationListenerHelper implements LocationListener {

    private static final String TAG = LocationListenerHelper.class.getSimpleName();
    private final float LOCATION_DISTANCE;
    private final int LOCATION_INTERVAL;
    private Context mContext;
    private LocationManager mLocationManager = null;
    private LocationListener[] locationListeners = new LocationListener[2];
    private IChangeLocationProviderChange iChangeLocation;

    public LocationListenerHelper(Context context, int locationInterval, float locationDistance, IChangeLocationProviderChange listener) {
        LOCATION_INTERVAL = locationInterval;
        LOCATION_DISTANCE = locationDistance;
        this.mContext = context;
        this.iChangeLocation = listener;
        if (mLocationManager == null)
            mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        initListeners();
    }

    public void connect() {
        if (mLocationManager != null) {
            try {
                mLocationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE, locationListeners[1]);
            } catch (java.lang.SecurityException ex) {
                if (BuildConfig.DEBUG)
                    Log.i(TAG, "fail to request location update, ignore", ex);
            } catch (IllegalArgumentException ex) {
                if (BuildConfig.DEBUG)
                    Log.d(TAG, "network provider does not exist, " + ex.getMessage());
            }
            try {
                mLocationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE, locationListeners[0]);
            } catch (java.lang.SecurityException ex) {
                if (BuildConfig.DEBUG)
                    Log.i(TAG, "fail to request location update, ignore", ex);
            } catch (IllegalArgumentException ex) {
                if (BuildConfig.DEBUG)
                    Log.d(TAG, "gps provider does not exist " + ex.getMessage());
            }
        }
    }

    public void disconnect() {
        if (mLocationManager != null) {
            try {
                mLocationManager.removeUpdates(locationListeners[0]);
            } catch (Exception ex) {
                if (BuildConfig.DEBUG)
                    Log.i(TAG, "fail to remove location listeners, ignore", ex);
            }
            try {
                mLocationManager.removeUpdates(locationListeners[1]);
            } catch (Exception ex) {
                if (BuildConfig.DEBUG)
                    Log.i(TAG, "fail to remove location listeners, ignore", ex);
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (Storage.getInstance(mContext).isAcceptableChangeWithSave(location)) {
            if (BuildConfig.DEBUG)
                Log.e(TAG, String.valueOf(location.getLatitude()) + " - " + String.valueOf(location.getLongitude()));
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
        if (iChangeLocation != null)
            iChangeLocation.onProviderEnabled();
    }

    @Override
    public void onProviderDisabled(String s) {
        if (!(mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)))
            if (iChangeLocation != null)
                iChangeLocation.onProvidersDisabled();
    }

    private void initListeners() {
        for (int i = 0; i < locationListeners.length; i++)
            locationListeners[i] = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    LocationListenerHelper.this.onLocationChanged(location);
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {
                    LocationListenerHelper.this.onStatusChanged(s, i, bundle);
                }

                @Override
                public void onProviderEnabled(String s) {
                    LocationListenerHelper.this.onProviderEnabled(s);
                }

                @Override
                public void onProviderDisabled(String s) {
                    LocationListenerHelper.this.onProviderDisabled(s);
                }
            };
    }
}
