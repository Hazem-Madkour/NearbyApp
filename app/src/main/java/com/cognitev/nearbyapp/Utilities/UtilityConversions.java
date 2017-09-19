package com.cognitev.nearbyapp.Utilities;

import android.content.ContentValues;
import android.database.Cursor;

import com.cognitev.nearbyapp.Data.VenueContract.VenuesColumns;
import com.cognitev.nearbyapp.Models.Venue;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class UtilityConversions {

    static public ArrayList<Venue> convertFromJSONResponseToListOfVenue(JSONObject response) throws Exception {
        ArrayList<Venue> lstVenues = new ArrayList<>();
        JSONArray jsonArray = response.getJSONObject("response").getJSONArray("groups").getJSONObject(0).getJSONArray("items");
        for (int index = 0; index < jsonArray.length(); index++) {
            lstVenues.add(convertFromJSONObjectToVenue(jsonArray.getJSONObject(index).getJSONObject("venue")));
        }
        return lstVenues;
    }

    static public Venue convertFromJSONObjectToVenue(JSONObject jsonVenue) throws Exception {
        Venue venue = new Venue();
        venue.id = jsonVenue.getString("id");
        venue.name = jsonVenue.getString("name");
        venue.lat = jsonVenue.getJSONObject("location").getDouble("lat");
        venue.lng = jsonVenue.getJSONObject("location").getDouble("lng");
        venue.formattedAddress = "";
        if (jsonVenue.getJSONArray("categories").length() > 0 && !jsonVenue.getJSONArray("categories").getJSONObject(0).isNull("name")) {
            venue.formattedAddress += jsonVenue.getJSONArray("categories").getJSONObject(0).getString("name") + " - ";
        }
        if (!jsonVenue.getJSONObject("location").isNull("formattedAddress")) {
            JSONArray jsonArrayAddress = jsonVenue.getJSONObject("location").getJSONArray("formattedAddress");
            for (int addressIndex = 0; addressIndex < jsonArrayAddress.length(); addressIndex++)
                venue.formattedAddress += addressIndex == jsonArrayAddress.length() - 1 ? jsonArrayAddress.getString(addressIndex) : jsonArrayAddress.getString(addressIndex) + " - ";
        }
        if (jsonVenue.getJSONObject("photos").getJSONArray("groups").length() > 0 && jsonVenue.getJSONObject("photos").getJSONArray("groups").getJSONObject(0).getJSONArray("items").length() > 0) {
            venue.suffix = jsonVenue.getJSONObject("photos").getJSONArray("groups").getJSONObject(0).getJSONArray("items").getJSONObject(0).getString("suffix");
            venue.prefix = jsonVenue.getJSONObject("photos").getJSONArray("groups").getJSONObject(0).getJSONArray("items").getJSONObject(0).getString("prefix");
        }
        return venue;
    }

    static public ContentValues[] convertFromVenuesToContentValues(ArrayList<Venue> lstVenues) {
        ContentValues[] VenueValues = new ContentValues[lstVenues.size()];
        ContentValues value;
        for (int i = 0; i < lstVenues.size(); i++) {
            value = new ContentValues();
            value.put(VenuesColumns.COLUMN_KEY, lstVenues.get(i).id);
            value.put(VenuesColumns.COLUMN_NAME, lstVenues.get(i).name);
            value.put(VenuesColumns.COLUMN_SUFFIX, lstVenues.get(i).suffix);
            value.put(VenuesColumns.COLUMN_PREFIX, lstVenues.get(i).prefix);
            value.put(VenuesColumns.COLUMN_LAT, lstVenues.get(i).lat);
            value.put(VenuesColumns.COLUMN_LNG, lstVenues.get(i).lng);
            value.put(VenuesColumns.COLUMN_FORMATTED_ADDRESS, lstVenues.get(i).formattedAddress);
            VenueValues[i] = value;
        }
        return VenueValues;
    }

    static public Venue getVenueFromCursor(Cursor cursor) {
        Venue venue = new Venue();
        venue.id = cursor.getString(UtilityDatabase.COL_VENUE_KEY);
        venue.formattedAddress = cursor.getString(UtilityDatabase.COL_VENUE_FORMATTED_ADDRESS);
        venue.name = cursor.getString(UtilityDatabase.COL_VENUE_NAME);
        venue.suffix = cursor.getString(UtilityDatabase.COL_VENUE_SUFFIX);
        venue.prefix = cursor.getString(UtilityDatabase.COL_VENUE_PREFIX);
        venue.lat = cursor.getDouble(UtilityDatabase.COL_VENUE_LAT);
        venue.lng = cursor.getDouble(UtilityDatabase.COL_VENUE_LNG);
        venue.id = cursor.getString(UtilityDatabase.COL_VENUE_KEY);
        return venue;
    }

    static public String getDateFormat(String format) {
        return new SimpleDateFormat(format).format(new Date());
    }
}
