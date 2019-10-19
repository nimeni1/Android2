package com.example.master.awesomeapplication;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by Master on 7/8/2017.
 */

public class User implements Serializable{

    private String Name;
    private String Email;
    private String Age;
    private String Gender;
    private String Password;
    private LatLng latLng;

    public User(){
        latLng = null;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }
}
