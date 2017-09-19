package com.cognitev.nearbyapp.Utilities;

import android.content.Context;
import android.location.Location;

import com.cognitev.nearbyapp.BuildConfig;
import com.cognitev.nearbyapp.Interfaces.ILoadObject;
import com.cognitev.nearbyapp.Models.Venue;
import com.cognitev.nearbyapp.R;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

public class UtilityServices {

    static void getNearbyLocations(final Context context, Location location, double radius, final ILoadObject<ArrayList<Venue>> listener) {
        listener.onInitialize();
        if (noNetworkWithFinishListener(context, listener)) return;
        if (isNullWithFinishListener(context, location, listener)) return;
        CallApi.GET(CallApi.SERVER_URL, CallApi.ROUTE_EXPLORE,
                getFourSquareParams(location.getLatitude(), location.getLongitude(), radius, true),
                null, CallApi.TIME_OUT, new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        handleOnFailure(context, statusCode, listener);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        try {
                            ArrayList<Venue> lstVenues = UtilityConversions.convertFromJSONResponseToListOfVenue(new JSONObject(responseString));
                            if (lstVenues.size() == 0)
                                listener.onFail(context.getString(R.string.warn_no_data_found));
                            else {
                                listener.onSuccess(lstVenues);
                            }
                        } catch (Exception ex) {
                            listener.onFail(context.getString(R.string.warn_something_went_wrong));
                        }
                        listener.onFinish();
                    }
                });
    }

    //region helper methods
    private static HashMap<String, String> getFourSquareParams(double latitude, double longitude, double radius, boolean withPhotos) {
        HashMap<String, String> params = new HashMap<>();
        params.put(CallApi.LATITUDE_LONGITUDE_PARAM, String.valueOf(latitude) + "," + String.valueOf(longitude));
        params.put(CallApi.RADIUS_PARAM, String.valueOf(radius));
        params.put(CallApi.CLIENT_ID_PARAM, BuildConfig.FOUR_SQUARE_CLIENT_ID);
        params.put(CallApi.CLIENT_SECRET_PARAM, BuildConfig.FOUR_SQUARE_CLIENT_SECRET);
        params.put(CallApi.VENUE_PHOTOS_PARAM, withPhotos ? "1" : "0");
        params.put("v", UtilityConversions.getDateFormat("yyyyMMdd"));
        return params;
    }

    private static boolean noNetworkWithFinishListener(Context context, ILoadObject listener) {
        if (!UtilityGeneral.isOnline(context)) {
            listener.onFail(context.getString(R.string.warn_no_internet));
            listener.onFinish();
            return true;
        }
        return false;
    }

    private static boolean isNullWithFinishListener(Context context, Object object, ILoadObject listener) {
        if (object == null) {
            listener.onFail(context.getString(R.string.warn_something_went_wrong));
            listener.onFinish();
            return true;
        }
        return false;
    }

    private static void handleOnFailure(Context context, int statusCode, ILoadObject listener) {
        if (statusCode == 0)
            listener.onFail(context.getString(R.string.warn_no_internet));
        else
            listener.onFail(context.getString(R.string.warn_something_went_wrong));
        listener.onFinish();
    }
    //endregion
}
