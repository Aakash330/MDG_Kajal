package com.techive.mydailygoodscustomer.Models;

import com.google.gson.annotations.SerializedName;

public class StoreListByCityName_Data_ReviewData {

    @SerializedName("star")
    private float star;

    @SerializedName("reviewMsg")
    private String reviewMsg;

    public StoreListByCityName_Data_ReviewData(float star, String reviewMsg) {
        this.star = star;
        this.reviewMsg = reviewMsg;
    }

    public float getStar() {
        return star;
    }

    public void setStar(float star) {
        this.star = star;
    }

    public String getReviewMsg() {
        return reviewMsg;
    }

    public void setReviewMsg(String reviewMsg) {
        this.reviewMsg = reviewMsg;
    }

    @Override
    public String toString() {
        return "StoreListByCityName_Data_ReviewData{" +
                "star=" + star +
                ", reviewMsg='" + reviewMsg + '\'' +
                '}';
    }
}
