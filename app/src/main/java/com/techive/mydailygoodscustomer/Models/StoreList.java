package com.techive.mydailygoodscustomer.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StoreList {

    @SerializedName("error")
    private int error;

    @SerializedName("msg")
    private String msg;

    @SerializedName("data")
    private List<StoreList_Data> data;

    public StoreList(int error, String msg, List<StoreList_Data> data) {
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

    public List<StoreList_Data> getData() {
        return data;
    }

    public void setData(List<StoreList_Data> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "StoreList{" +
                "error=" + error +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
