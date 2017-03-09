package com.fhf.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by santosh on 3/7/2017.
 */

public class User implements Serializable {

    public String facebookID;
    public String gender;

    @SerializedName("email")
    public String email;
    @SerializedName("username")
    public String username;
    @SerializedName("userid")
    public String userid;
    @SerializedName("phone_number")
    public String phone_number;
    @SerializedName("message")
    private String message;

    public String getFacebookID() {
        return facebookID;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getGender() {
        return gender;
    }

    public void setFacebookID(String facebookID) {
        this.facebookID = facebookID;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
