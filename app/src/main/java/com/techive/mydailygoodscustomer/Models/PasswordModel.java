package com.techive.mydailygoodscustomer.Models;

import com.google.gson.annotations.SerializedName;

public class PasswordModel {

    /*user:152
oldpassword:admin@1223
password:admin@12234
password_confirmation:admin@12234*/

    /*FORGOT PASSWORD.*/
    @SerializedName("mobile")
    private String mobile;

    /*CHANGE PASSWORD*/
    @SerializedName("user")
    private int user;

    /*CHANGE PASSWORD*/
    @SerializedName("oldpassword")
    private String oldpassword;

    /*CHANGE PASSWORD*/
    /*FORGOT PASSWORD.*/
    @SerializedName("password")
    private String password;

    /*CHANGE PASSWORD*/
    /*FORGOT PASSWORD.*/
    @SerializedName("password_confirmation")
    private String password_confirmation;

    public PasswordModel(String mobile, int user, String oldpassword, String password, String password_confirmation) {
        this.mobile = mobile;
        this.user = user;
        this.oldpassword = oldpassword;
        this.password = password;
        this.password_confirmation = password_confirmation;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public String getOldpassword() {
        return oldpassword;
    }

    public void setOldpassword(String oldpassword) {
        this.oldpassword = oldpassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword_confirmation() {
        return password_confirmation;
    }

    public void setPassword_confirmation(String password_confirmation) {
        this.password_confirmation = password_confirmation;
    }

    @Override
    public String toString() {
        return "PasswordModel{" +
                "mobile='" + mobile + '\'' +
                ", user=" + user +
                ", oldpassword='" + oldpassword + '\'' +
                ", password='" + password + '\'' +
                ", password_confirmation='" + password_confirmation + '\'' +
                '}';
    }
}
