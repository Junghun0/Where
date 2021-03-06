package com.example.parkjunghun.where.where.Model;

/**
 * Created by parkjunghun on 2017. 11. 21..
 */

public class User {
    private String email;
    private String password;
    private String phonenum;
    private String name;
    private String uid;
    private double latitude;
    private double longitude;

    public User(){
    }

    public User(String email,String password, String phonenum,String name,String uid,double latitude,double longitude){
        this.email  = email;
        this.password = password;
        this.phonenum = phonenum;
        this.name = name;
        this.uid = uid;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getUid() { return uid; }

    public void setUid(String uid) { this.uid = uid; }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    public String getName(){return name;}

    public void setName(String name){this.name=name;}
}
