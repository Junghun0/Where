package com.example.parkjunghun.where.where.Model;

/**
 * Created by parkjunghun on 2017. 11. 29..
 */

public class Locationinfo {
    String latitude;
    String longitude;
    String email;

    public Locationinfo(String latitude, String longitude, String email) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.email = email;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
