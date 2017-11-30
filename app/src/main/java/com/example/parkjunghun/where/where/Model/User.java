package com.example.parkjunghun.where.where.Model;

/**
 * Created by parkjunghun on 2017. 11. 21..
 */

public class User {
    public String email;
    public String password;
    public String phonenum;
    public String name;

    public User(){
    }

    public User(String email,String password, String phonenum,String name){
        this.email  = email;
        this.password = password;
        this.phonenum = phonenum;
        this.name = name;
    }

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
