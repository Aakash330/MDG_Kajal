package com.techive.mydailygoodscustomer.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StoreListByCityName {

    @SerializedName("error")
    private int error;

    @SerializedName("msg")
    private String msg;

    @SerializedName("data")
    private List<StoreListByCityName_Data> data;

    public StoreListByCityName(int error, String msg, List<StoreListByCityName_Data> data) {
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

    public List<StoreListByCityName_Data> getData() {
        return data;
    }

    public void setData(List<StoreListByCityName_Data> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "StoreListByCityName{" +
                "error=" + error +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
