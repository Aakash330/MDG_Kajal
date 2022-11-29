package com.techive.mydailygoodscustomer.Models;

import com.google.gson.annotations.SerializedName;

public class RateStoreModel {

        /*buy_id:
vId:
rate_value:
reviewsText:*/

    @SerializedName("buy_id")
    private int buy_id;

    @SerializedName("vId")
    private int vId;

    @SerializedName("rate_value")
    private float rate_value;

    @SerializedName("reviewsText")
    private String reviewsText;

    public RateStoreModel(int buy_id, int vId, float rate_value, String reviewsText) {
        this.buy_id = buy_id;
        this.vId = vId;
        this.rate_value = rate_value;
        this.reviewsText = reviewsText;
    }

    public int getBuy_id() {
        return buy_id;
    }

    public void setBuy_id(int buy_id) {
        this.buy_id = buy_id;
    }

    public int getvId() {
        return vId;
    }

    public void setvId(int vId) {
        this.vId = vId;
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

    @Override
    public String toString() {
        return "RateStoreModel{" +
                "buy_id=" + buy_id +
                ", vId=" + vId +
                ", rate_value=" + rate_value +
                ", reviewsText=" + reviewsText +
                '}';
    }
}
