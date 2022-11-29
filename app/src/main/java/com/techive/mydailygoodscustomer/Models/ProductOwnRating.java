package com.techive.mydailygoodscustomer.Models;

import com.google.gson.annotations.SerializedName;

public class ProductOwnRating {

    /*{
    "msg": "success!",
    "data": {
        "star": 4,
        "text": "good"
    },
    "error": 200
}*/

    @SerializedName("msg")
    private String msg;

    @SerializedName("data")
    private ProductOwnRating_Data data;

    @SerializedName("error")
    private int error;

    public ProductOwnRating(String msg, ProductOwnRating_Data data, int error) {
        this.msg = msg;
        this.data = data;
        this.error = error;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ProductOwnRating_Data getData() {
        return data;
    }

    public void setData(ProductOwnRating_Data data) {
        this.data = data;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "ProductOwnRating{" +
                "msg='" + msg + '\'' +
                ", data=" + data +
                ", error=" + error +
                '}';
    }
}
