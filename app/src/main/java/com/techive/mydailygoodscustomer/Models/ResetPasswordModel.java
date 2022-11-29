package com.techive.mydailygoodscustomer.Models;

import com.google.gson.annotations.SerializedName;

public class ResetPasswordModel {

    /*userId:
login_type:
password:*/

    @SerializedName("userId")
    private int userId;

    @SerializedName("login_type")
    private int login_type;

    @SerializedName("password")
    private String password;

    public ResetPasswordModel(int userId, int login_type, String password) {
        this.userId = userId;
        this.login_type = login_type;
        this.password = password;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getLogin_type() {
        return login_type;
    }

    public void setLogin_type(int login_type) {
        this.login_type = login_type;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "ResetPasswordModel{" +
                "userId=" + userId +
                ", login_type=" + login_type +
                ", password='" + password + '\'' +
                '}';
    }
}
