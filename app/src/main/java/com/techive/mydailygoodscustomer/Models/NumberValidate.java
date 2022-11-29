package com.techive.mydailygoodscustomer.Models;

import com.google.gson.annotations.SerializedName;

public class NumberValidate {

    /*{
    "msg": "Number not registerd!",
    "userId": null,
    "type": null,
    "error": 402
}*/

    @SerializedName("msg")
    private String msg;

    @SerializedName("userId")
    private Integer userId;

    @SerializedName("type")
    private Integer type;

    @SerializedName("error")
    private int error;

    public NumberValidate(String msg, Integer userId, Integer type, int error) {
        this.msg = msg;
        this.userId = userId;
        this.type = type;
        this.error = error;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "NumberValidate{" +
                "msg='" + msg + '\'' +
                ", userId=" + userId +
                ", type=" + type +
                ", error=" + error +
                '}';
    }
}
