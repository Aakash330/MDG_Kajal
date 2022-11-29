package com.techive.mydailygoodscustomer.Models;

import com.google.gson.annotations.SerializedName;

public class HomeModel_CouponData {

    @SerializedName("coupon")
    private String coupon;

    public HomeModel_CouponData(String coupon) {
        this.coupon = coupon;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    @Override
    public String toString() {
        return "HomeModel_CouponData{" +
                "coupon='" + coupon + '\'' +
                '}';
    }
}
