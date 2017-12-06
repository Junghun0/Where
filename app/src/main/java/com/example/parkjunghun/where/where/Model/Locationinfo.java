package com.example.parkjunghun.where.where.Model;

/**
 * Created by parkjunghun on 2017. 11. 29..
 */

public class Locationinfo {
    private String latitude;
    private String longitude;
    private String email;

    public Locationinfo(){}

    public Locationinfo(String latitude, String longitude, String email) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.email = email;
    }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

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
