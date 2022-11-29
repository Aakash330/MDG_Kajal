package com.techive.mydailygoodscustomer.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CityList {

    @SerializedName("error")
    private int error;

    @SerializedName("msg")
    private String msg;

    @SerializedName("data")
    private List<CityList_Data> data;

    public CityList(int error, String msg, List<CityList_Data> data) {
        this.error = error;
        this.msg = msg;
        this.data = data;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<CityList_Data> getData() {
        return data;
    }

    public void setData(List<CityList_Data> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "CityList{" +
                "error=" + error +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
