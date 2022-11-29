package com.techive.mydailygoodscustomer.Models;

import com.google.gson.annotations.SerializedName;

public class ViewShopRating_Data {

    /*{
            "name": "Abhi Kumar",
            "star": 3,
            "reviewMsg": "Bad experience"
        }*/

    @SerializedName("name")
    private String name;

    @SerializedName("star")
    private int star;

    @SerializedName("reviewMsg")
    private String reviewMsg;

    public ViewShopRating_Data(String name, int star, String reviewMsg) {
        this.name = name;
        this.star = star;
        this.reviewMsg = reviewMsg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
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
        return "ViewShopRating_Data{" +
                "name='" + name + '\'' +
                ", star=" + star +
                ", reviewMsg='" + reviewMsg + '\'' +
                '}';
    }
}
