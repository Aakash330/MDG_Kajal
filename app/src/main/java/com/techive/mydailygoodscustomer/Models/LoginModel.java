package com.techive.mydailygoodscustomer.Models;

import com.google.gson.annotations.SerializedName;

public class LoginModel {

    @SerializedName("mobile")
    private String mobile;

    @SerializedName("password")
    private String password;

    @SerializedName("token")
    private String token;

    public LoginModel(String mobile, String password, String token) {
        this.mobile = mobile;
        this.password = password;
        this.token = token;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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
        return "LoginModel{" +
                "mobile='" + mobile + '\'' +
                ", password='" + password + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
