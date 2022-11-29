package com.techive.mydailygoodscustomer.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HomeModel {

    @SerializedName("error")
    private int error;

    @SerializedName("msg")
    private String msg;

    @SerializedName("store_name")
    private String store_name;

    @SerializedName("logo")
    private String logo;

    @SerializedName("banner_data")
    private List<HomeModel_BannerData> banner_data;

    @SerializedName("section_banner")
    private HomeModel_BannerData section_banner;

    @SerializedName("coupon_data")
    private List<HomeModel_CouponData> coupon_data;

    @SerializedName("category_data")
    private List<HomeModel_CategoryData> category_data;

    public HomeModel(int error, String msg, String store_name, String logo, List<HomeModel_BannerData> banner_data, HomeModel_BannerData section_banner, List<HomeModel_CouponData> coupon_data, List<HomeModel_CategoryData> category_data) {
        this.error = error;
        this.msg = msg;
        this.store_name = store_name;
        this.logo = logo;
        this.banner_data = banner_data;
        this.section_banner = section_banner;
        this.coupon_data = coupon_data;
        this.category_data = category_data;
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

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public List<HomeModel_BannerData> getBanner_data() {
        return banner_data;
    }

    public void setBanner_data(List<HomeModel_BannerData> banner_data) {
        this.banner_data = banner_data;
    }

    public HomeModel_BannerData getSection_banner() {
        return section_banner;
    }

    public void setSection_banner(HomeModel_BannerData section_banner) {
        this.section_banner = section_banner;
    }

    public List<HomeModel_CouponData> getCoupon_data() {
        return coupon_data;
    }

    public void setCoupon_data(List<HomeModel_CouponData> coupon_data) {
        this.coupon_data = coupon_data;
    }

    public List<HomeModel_CategoryData> getCategory_data() {
        return category_data;
    }

    public void setCategory_data(List<HomeModel_CategoryData> category_data) {
        this.category_data = category_data;
    }

    @Override
    public String toString() {
        return "HomeModel{" +
                "error=" + error +
                ", msg='" + msg + '\'' +
                ", store_name='" + store_name + '\'' +
                ", logo='" + logo + '\'' +
                ", banner_data=" + banner_data +
                ", section_banner=" + section_banner +
                ", coupon_data=" + coupon_data +
                ", category_data=" + category_data +
                '}';
    }
}
