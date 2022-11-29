package com.techive.mydailygoodscustomer.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StoreListByName {

    @SerializedName("error")
    private int error;

    @SerializedName("msg")
    private String msg;

    @SerializedName("data")
    private List<StoreListByName_Data> data;

    public StoreListByName(int error, String msg, List<StoreListByName_Data> data) {
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

    public List<StoreListByName_Data> getData() {
        return data;
    }

    public void setData(List<StoreListByName_Data> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "StoreListByName{" +
                "error=" + error +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
