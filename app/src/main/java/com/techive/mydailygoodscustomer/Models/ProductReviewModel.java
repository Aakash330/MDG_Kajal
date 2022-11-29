package com.techive.mydailygoodscustomer.Models;

import com.google.gson.annotations.SerializedName;

public class ProductReviewModel {

    /*prod_id:4556
user_id:126
rate_value:2
reviewsText:hvhjvj
status:1*/

    @SerializedName("prod_id")
    private int prod_id;

    @SerializedName("user_id")
    private int user_id;

    @SerializedName("rate_value")
    private float rate_value;

    @SerializedName("reviewsText")
    private String reviewsText;

    @SerializedName("status")
    private int status;

    public ProductReviewModel(int prod_id, int user_id, float rate_value, String reviewsText, int status) {
        this.prod_id = prod_id;
        this.user_id = user_id;
        this.rate_value = rate_value;
        this.reviewsText = reviewsText;
        this.status = status;
    }

    public int getProd_id() {
        return prod_id;
    }

    public void setProd_id(int prod_id) {
        this.prod_id = prod_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public float getRate_value() {
        return rate_value;
    }

    public void setRate_value(float rate_value) {
        this.rate_value = rate_value;
    }

    public String getReviewsText() {
        return reviewsText;
    }

    public void setReviewsText(String reviewsText) {
        this.reviewsText = reviewsText;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ProductReviewModel{" +
                "prod_id=" + prod_id +
                ", user_id=" + user_id +
                ", rate_value=" + rate_value +
                ", reviewsText='" + reviewsText + '\'' +
                ", status=" + status +
                '}';
    }
}
