package com.techive.mydailygoodscustomer.Models;

import com.google.gson.annotations.SerializedName;

public class StoreListByCityName_Data {

    @SerializedName("store")
    private String store;

    @SerializedName("store_id")
    private int store_id;

    @SerializedName("address")
    private String address;

    @SerializedName("website")
    private String website;

    @SerializedName("mob_no1")
    private String mob_no1;

    @SerializedName("mob_no2")
    private String mob_no2;

    @SerializedName("whap_no1")
    private String whap_no1;

    @SerializedName("whap_no2")
    private String whap_no2;

    @SerializedName("homedeliver")
    private int homedeliver;

    @SerializedName("sun_o")
    private String sun_o;

    @SerializedName("mon_o")
    private String mon_o;

    @SerializedName("tue_o")
    private String tue_o;

    @SerializedName("wed_o")
    private String wed_o;

    @SerializedName("thu_o")
    private String thu_o;

    @SerializedName("fri_o")
    private String fri_o;

    @SerializedName("sat_o")
    private String sat_o;

    @SerializedName("sun_c")
    private String sun_c;

    @SerializedName("mon_c")
    private String mon_c;

    @SerializedName("tue_c")
    private String tue_c;

    @SerializedName("wed_c")
    private String wed_c;

    @SerializedName("thu_c")
    private String thu_c;

    @SerializedName("fri_c")
    private String fri_c;

    @SerializedName("sat_c")
    private String sat_c;

    @SerializedName("image1")
    private String image1;

    @SerializedName("image2")
    private String image2;

    @SerializedName("image3")
    private String image3;

    @SerializedName("image4")
    private String image4;

    @SerializedName("image5")
    private String image5;

    @SerializedName("state")
    private String state;

    @SerializedName("city")
    private String city;

    @SerializedName("total_rating")
    private int total_rating;

    @SerializedName("avg_rating")
    private String avg_rating;

    /*ONLY USED IN FAV STORES LIST*/
    @SerializedName("fav_status")
    private int fav_status;

    /*ONLY USED IN FAV STORES LIST*/
    @SerializedName("total_delivery_count")
    private Integer total_delivery_count;

    /*ONLY USED IN FAV STORES LIST*/
    @SerializedName("reviewData")
    private StoreListByCityName_Data_ReviewData reviewData;

    public StoreListByCityName_Data(String store, int store_id, String address, String website, String mob_no1, String mob_no2, String whap_no1, String whap_no2, int homedeliver, String sun_o, String mon_o, String tue_o, String wed_o, String thu_o, String fri_o, String sat_o, String sun_c, String mon_c, String tue_c, String wed_c, String thu_c, String fri_c, String sat_c, String image1, String image2, String image3, String image4, String image5, String state, String city, int total_rating, String avg_rating, int fav_status, Integer total_delivery_count, StoreListByCityName_Data_ReviewData reviewData) {
        this.store = store;
        this.store_id = store_id;
        this.address = address;
        this.website = website;
        this.mob_no1 = mob_no1;
        this.mob_no2 = mob_no2;
        this.whap_no1 = whap_no1;
        this.whap_no2 = whap_no2;
        this.homedeliver = homedeliver;
        this.sun_o = sun_o;
        this.mon_o = mon_o;
        this.tue_o = tue_o;
        this.wed_o = wed_o;
        this.thu_o = thu_o;
        this.fri_o = fri_o;
        this.sat_o = sat_o;
        this.sun_c = sun_c;
        this.mon_c = mon_c;
        this.tue_c = tue_c;
        this.wed_c = wed_c;
        this.thu_c = thu_c;
        this.fri_c = fri_c;
        this.sat_c = sat_c;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.image4 = image4;
        this.image5 = image5;
        this.state = state;
        this.city = city;
        this.total_rating = total_rating;
        this.avg_rating = avg_rating;
        this.fav_status = fav_status;
        this.total_delivery_count = total_delivery_count;
        this.reviewData = reviewData;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getMob_no1() {
        return mob_no1;
    }

    public void setMob_no1(String mob_no1) {
        this.mob_no1 = mob_no1;
    }

    public String getMob_no2() {
        return mob_no2;
    }

    public void setMob_no2(String mob_no2) {
        this.mob_no2 = mob_no2;
    }

    public String getWhap_no1() {
        return whap_no1;
    }

    public void setWhap_no1(String whap_no1) {
        this.whap_no1 = whap_no1;
    }

    public String getWhap_no2() {
        return whap_no2;
    }

    public void setWhap_no2(String whap_no2) {
        this.whap_no2 = whap_no2;
    }

    public int getHomedeliver() {
        return homedeliver;
    }

    public void setHomedeliver(int homedeliver) {
        this.homedeliver = homedeliver;
    }

    public String getSun_o() {
        return sun_o;
    }

    public void setSun_o(String sun_o) {
        this.sun_o = sun_o;
    }

    public String getMon_o() {
        return mon_o;
    }

    public void setMon_o(String mon_o) {
        this.mon_o = mon_o;
    }

    public String getTue_o() {
        return tue_o;
    }

    public void setTue_o(String tue_o) {
        this.tue_o = tue_o;
    }

    public String getWed_o() {
        return wed_o;
    }

    public void setWed_o(String wed_o) {
        this.wed_o = wed_o;
    }

    public String getThu_o() {
        return thu_o;
    }

    public void setThu_o(String thu_o) {
        this.thu_o = thu_o;
    }

    public String getFri_o() {
        return fri_o;
    }

    public void setFri_o(String fri_o) {
        this.fri_o = fri_o;
    }

    public String getSat_o() {
        return sat_o;
    }

    public void setSat_o(String sat_o) {
        this.sat_o = sat_o;
    }

    public String getSun_c() {
        return sun_c;
    }

    public void setSun_c(String sun_c) {
        this.sun_c = sun_c;
    }

    public String getMon_c() {
        return mon_c;
    }

    public void setMon_c(String mon_c) {
        this.mon_c = mon_c;
    }

    public String getTue_c() {
        return tue_c;
    }

    public void setTue_c(String tue_c) {
        this.tue_c = tue_c;
    }

    public String getWed_c() {
        return wed_c;
    }

    public void setWed_c(String wed_c) {
        this.wed_c = wed_c;
    }

    public String getThu_c() {
        return thu_c;
    }

    public void setThu_c(String thu_c) {
        this.thu_c = thu_c;
    }

    public String getFri_c() {
        return fri_c;
    }

    public void setFri_c(String fri_c) {
        this.fri_c = fri_c;
    }

    public String getSat_c() {
        return sat_c;
    }

    public void setSat_c(String sat_c) {
        this.sat_c = sat_c;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getImage4() {
        return image4;
    }

    public void setImage4(String image4) {
        this.image4 = image4;
    }

    public String getImage5() {
        return image5;
    }

    public void setImage5(String image5) {
        this.image5 = image5;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getTotal_rating() {
        return total_rating;
    }

    public void setTotal_rating(int total_rating) {
        this.total_rating = total_rating;
    }

    public String getAvg_rating() {
        return avg_rating;
    }

    public void setAvg_rating(String avg_rating) {
        this.avg_rating = avg_rating;
    }

    public int getFav_status() {
        return fav_status;
    }

    public void setFav_status(int fav_status) {
        this.fav_status = fav_status;
    }

    public Integer getTotal_delivery_count() {
        return total_delivery_count;
    }

    public void setTotal_delivery_count(Integer total_delivery_count) {
        this.total_delivery_count = total_delivery_count;
    }

    public StoreListByCityName_Data_ReviewData getReviewData() {
        return reviewData;
    }

    public void setReviewData(StoreListByCityName_Data_ReviewData reviewData) {
        this.reviewData = reviewData;
    }

    @Override
    public String toString() {
        return "StoreListByCityName_Data{" +
                "store='" + store + '\'' +
                ", store_id=" + store_id +
                ", address='" + address + '\'' +
                ", website='" + website + '\'' +
                ", mob_no1='" + mob_no1 + '\'' +
                ", mob_no2='" + mob_no2 + '\'' +
                ", whap_no1='" + whap_no1 + '\'' +
                ", whap_no2='" + whap_no2 + '\'' +
                ", homedeliver=" + homedeliver +
                ", sun_o='" + sun_o + '\'' +
                ", mon_o='" + mon_o + '\'' +
                ", tue_o='" + tue_o + '\'' +
                ", wed_o='" + wed_o + '\'' +
                ", thu_o='" + thu_o + '\'' +
                ", fri_o='" + fri_o + '\'' +
                ", sat_o='" + sat_o + '\'' +
                ", sun_c='" + sun_c + '\'' +
                ", mon_c='" + mon_c + '\'' +
                ", tue_c='" + tue_c + '\'' +
                ", wed_c='" + wed_c + '\'' +
                ", thu_c='" + thu_c + '\'' +
                ", fri_c='" + fri_c + '\'' +
                ", sat_c='" + sat_c + '\'' +
                ", image1='" + image1 + '\'' +
                ", image2='" + image2 + '\'' +
                ", image3='" + image3 + '\'' +
                ", image4='" + image4 + '\'' +
                ", image5='" + image5 + '\'' +
                ", state='" + state + '\'' +
                ", city='" + city + '\'' +
                ", total_rating=" + total_rating +
                ", avg_rating='" + avg_rating + '\'' +
                ", fav_status=" + fav_status +
                ", total_delivery_count=" + total_delivery_count +
                ", reviewData=" + reviewData +
                '}';
    }
}
