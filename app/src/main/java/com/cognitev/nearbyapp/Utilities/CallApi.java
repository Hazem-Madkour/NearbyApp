package com.cognitev.nearbyapp.Utilities;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.HashMap;
import java.util.Map;

public class CallApi {

    //region constants server routes
    final static String SERVER_URL = "https://api.foursquare.com/v2/venues";
    final static String ROUTE_EXPLORE = "explore";
    //endregion

    //region constants server params
    final static String LATITUDE_LONGITUDE_PARAM = "ll";
    final static String RADIUS_PARAM = "radius";
    final static String CLIENT_ID_PARAM = "client_id";
    final static String CLIENT_SECRET_PARAM = "client_secret";
    final static String VENUE_PHOTOS_PARAM = "venuePhotos";
    //endregion


    final static int TIME_OUT = 120000;

    static void GET(String serverUrl, String route, HashMap<String, String> params, HashMap<String, String> headers, int timeOut, TextHttpResponseHandler handler) {
        ClientBuilder(headers, timeOut).get(UrlBuilder(serverUrl, route, params), handler);
    }

    //region helper methods
    static private AsyncHttpClient ClientBuilder(HashMap<String, String> headers, int timeOut) {
        AsyncHttpClient client = new AsyncHttpClient();
        if (headers != null)
            for (Map.Entry<String, String> entry : headers.entrySet())
                try {
                    client.addHeader(entry.getKey(), entry.getValue());
                } catch (Exception ignored) {
                }
        client.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        client.addHeader("Pragma", "no-cache");
        client.addHeader("Expires", "0");
        client.setTimeout(timeOut);
        client.setConnectTimeout(timeOut);
        client.setResponseTimeout(timeOut);
        return client;
    }

    static private String UrlBuilder(String serverUrl, String route, HashMap<String, String> params) {
        String url = serverUrl == null ? SERVER_URL : serverUrl;
        url += route == null ? "" : "/" + route;
        if (params != null && params.size() != 0) {
            url += "?";
            for (Map.Entry<String, String> entry : params.entrySet())
                url += entry.getKey() + "=" + entry.getValue() + "&";
            url = url.substring(0, url.length() - 1);
        }
        return url;
    }
    //endregion
}
