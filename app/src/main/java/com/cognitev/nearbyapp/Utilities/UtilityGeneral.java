package com.cognitev.nearbyapp.Utilities;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.location.Location;
import android.net.ConnectivityManager;

import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.cognitev.nearbyapp.Interfaces.ILoadObject;
import com.cognitev.nearbyapp.Models.Venue;
import com.cognitev.nearbyapp.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

public class UtilityGeneral {

    public static final float LOCATION_LISTENER_MIN_DISTANCE = 500;     // in meters
    public static final int LOCATION_LISTENER_INTERVAL = 10000;         //in Milliseconds
    public static final int FOURSQUARE_RADIUS = 1000;

    static public boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    static public double calculateDistanceInMeters(Location locationA, Location locationB) {
        return locationA.distanceTo(locationB);
    }

    static public Observable<String> downloadVenuesObservable(final Context context) {
        return Observable.create(
                new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(@NonNull final ObservableEmitter<String> e) throws Exception {
                        UtilityServices.getNearbyLocations(context, Storage.getInstance(context).loadLastLocation(), FOURSQUARE_RADIUS, new ILoadObject<ArrayList<Venue>>() {
                            @Override
                            public void onFinish() {
                                e.onComplete();
                            }

                            @Override
                            public void onSuccess(ArrayList<Venue> loadedObject) {
                                UtilityDatabase.insertVenues(context, loadedObject);
                                e.onNext("");
                            }

                            @Override
                            public void onFail(String errorMessage) {
                                e.onNext(errorMessage);
                            }
                        });
                    }
                });
    }

    static public void showLocationSettingsRequest(final Activity activity) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(activity.getBaseContext())
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@android.support.annotation.NonNull LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(activity, 1);
                        } catch (IntentSender.SendIntentException ignored) {
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }

    static public RequestOptions GetGlideOptions() {
        return new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.ic_gallery)
                .error(R.drawable.ic_store)
                .priority(Priority.HIGH);
    }
}
