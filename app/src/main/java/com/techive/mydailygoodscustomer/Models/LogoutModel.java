package com.techive.mydailygoodscustomer.Models;

import com.google.gson.annotations.SerializedName;

public class LogoutModel {

    @SerializedName("user_id")
    private int user_id;

    @SerializedName("token")
    private String token;

    public LogoutModel(int user_id, String token) {
        this.user_id = user_id;
        this.token = token;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "LogoutModel{" +
                "user_id=" + user_id +
                ", token='" + token + '\'' +
                '}';
    }
}
