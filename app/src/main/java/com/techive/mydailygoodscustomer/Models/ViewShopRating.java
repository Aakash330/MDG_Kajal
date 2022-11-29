package com.techive.mydailygoodscustomer.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ViewShopRating {

    /*{
    "msg": "Success!",
    "data": [
        {
            "name": "Abhi Kumar",
            "star": 3,
            "reviewMsg": "Bad experience"
        },
        {
            "name": "Vishal",
            "star": 3,
            "reviewMsg": "Thik thak"
        }
    ],
    "error": 200
}*/
    @SerializedName("msg")
    private String msg;

    @SerializedName("data")
    private List<ViewShopRating_Data> data;

    @SerializedName("average")
    private float average;

    @SerializedName("error")
    private int error;

    public ViewShopRating(String msg, List<ViewShopRating_Data> data, float average, int error) {
        this.msg = msg;
        this.data = data;
        this.average = average;
        this.error = error;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<ViewShopRating_Data> getData() {
        return data;
    }

    public void setData(List<ViewShopRating_Data> data) {
        this.data = data;
    }

    public float getAverage() {
        return average;
    }

    public void setAverage(float average) {
        this.average = average;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "ViewShopRating{" +
                "msg='" + msg + '\'' +
                ", data=" + data +
                ", average=" + average +
                ", error=" + error +
                '}';
    }
}
