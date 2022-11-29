package com.techive.mydailygoodscustomer.Models;

import com.google.gson.annotations.SerializedName;

public class RegisterModel {

    @SerializedName("fname")
    private String fname;

    @SerializedName("lname")
    private String lname;

    @SerializedName("mobile")
    private String mobile;

    @SerializedName("email")
    private String email;

    @SerializedName("buyerAddress")
    private String buyerAddress;

    @SerializedName("landmark")
    private String landmark;

    @SerializedName("lat")
    private String latitude;

    @SerializedName("long")
    private String longitude;

    @SerializedName("state_id")
    private int state_id;

    @SerializedName("city_id")
    private int city_id;

    @SerializedName("pin_code")
    private String pin_code;

    @SerializedName("password")
    private String password;

    @SerializedName("token")
    private String token;

    public RegisterModel(String fname, String lname, String mobile, String email, String buyerAddress, String landmark, String latitude, String longitude, int state_id, int city_id, String pin_code, String password, String token) {
        this.fname = fname;
        this.lname = lname;
        this.mobile = mobile;
        this.email = email;
        this.buyerAddress = buyerAddress;
        this.landmark = landmark;
        this.latitude = latitude;
        this.longitude = longitude;
        this.state_id = state_id;
        this.city_id = city_id;
        this.pin_code = pin_code;
        this.password = password;
        this.token = token;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBuyerAddress() {
        return buyerAddress;
    }

    public void setBuyerAddress(String buyerAddress) {
        this.buyerAddress = buyerAddress;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
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

    public int getState_id() {
        return state_id;
    }

    public void setState_id(int state_id) {
        this.state_id = state_id;
    }

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public String getPin_code() {
        return pin_code;
    }

    public void setPin_code(String pin_code) {
        this.pin_code = pin_code;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "RegisterModel{" +
                "fname='" + fname + '\'' +
                ", lname='" + lname + '\'' +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", buyerAddress='" + buyerAddress + '\'' +
                ", landmark='" + landmark + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", state_id=" + state_id +
                ", city_id=" + city_id +
                ", pin_code='" + pin_code + '\'' +
                ", password='" + password + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
