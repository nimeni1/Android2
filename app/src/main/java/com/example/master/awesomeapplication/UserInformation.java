package com.example.master.awesomeapplication;

/**
 * Created by gaste on 5/16/2017.
 */

public class UserInformation {
    private String name;
    private double latitude;
    private double longitude;

    public UserInformation(){

    }


    public UserInformation(String name, double latitude, double longitude){
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName(){
        return this.name;
    }
    public double getLatitude(){
        return this.latitude;
    }

    public double getLongitude(){
        return this.longitude;
    }
}
