package com.binitshah.caps;

/**
 * Created by bshah on 5/21/2016.
 * Cap object
 */
public class Cap {
    double lon;
    double lat;
    double price;
    String title;
    String description;
    double rating;
    String time;
    boolean outdoor;

    public Cap(){

    }

    public Cap(double lon, double lat, double price, String title, String description, double rating, String time, boolean outdoor) {
        this.lon = lon;
        this.lat = lat;
        this.price = price;
        this.title = title;
        this.description = description;
        this.rating = rating;
        this.time = time;
        this.outdoor = outdoor;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isOutdoor() {
        return outdoor;
    }

    public void setOutdoor(boolean outdoor) {
        this.outdoor = outdoor;
    }
}
