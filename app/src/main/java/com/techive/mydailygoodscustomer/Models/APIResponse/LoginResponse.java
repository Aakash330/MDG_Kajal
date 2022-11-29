package com.techive.mydailygoodscustomer.Models.APIResponse;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("msg")
    private String msg;

    @SerializedName("error")
    private int error;

    @SerializedName("user_id")
    private int user_id;

    @SerializedName("name")
    private String name;

    public LoginResponse(String msg, int error, int user_id, String name) {
        this.msg = msg;
        this.error = error;
        this.user_id = user_id;
        this.name = name;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "msg='" + msg + '\'' +
                ", error=" + error +
                ", user_id=" + user_id +
                ", name='" + name + '\'' +
                '}';
    }
}
