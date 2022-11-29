package com.techive.mydailygoodscustomer.Models;

import com.google.gson.annotations.SerializedName;

public class ProductAllRatings_Data {

    /*{
            "name": "Abhishek",
            "star": 4,
            "reviewMsg": "good"
        }*/

    @SerializedName("name")
    private String name;

    @SerializedName("star")
    private String star;

    @SerializedName("reviewMsg")
    private String reviewMsg;

    public ProductAllRatings_Data(String name, String star, String reviewMsg) {
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

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
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
        return "ProductAllRatings_Data{" +
                "name='" + name + '\'' +
                ", star='" + star + '\'' +
                ", reviewMsg='" + reviewMsg + '\'' +
                '}';
    }
}
