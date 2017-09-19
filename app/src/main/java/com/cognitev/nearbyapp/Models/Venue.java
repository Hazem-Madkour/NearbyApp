package com.cognitev.nearbyapp.Models;

public class Venue {
    public String id;
    public String name;
    public String formattedAddress;
    public String suffix;
    public String prefix;
    public double lat;
    public double lng;

    public String getImageUrl() {
        return prefix + "500x500" + suffix;
    }
}
