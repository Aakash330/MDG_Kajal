package com.techive.mydailygoodscustomer.Models;

import com.google.gson.annotations.SerializedName;

public class HomeModel_BannerData {

    @SerializedName("banner_id")
    private int banner_id;

    @SerializedName("mobile_banner")
    private String mobile_banner;

    @SerializedName("heading")
    private String heading;

    @SerializedName("sub_heading")
    private String sub_heading;

    public HomeModel_BannerData(int banner_id, String mobile_banner, String heading, String sub_heading) {
        this.banner_id = banner_id;
        this.mobile_banner = mobile_banner;
        this.heading = heading;
        this.sub_heading = sub_heading;
    }

    public int getBanner_id() {
        return banner_id;
    }

    public void setBanner_id(int banner_id) {
        this.banner_id = banner_id;
    }

    public String getMobile_banner() {
        return mobile_banner;
    }

    public void setMobile_banner(String mobile_banner) {
        this.mobile_banner = mobile_banner;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getSub_heading() {
        return sub_heading;
    }

    public void setSub_heading(String sub_heading) {
        this.sub_heading = sub_heading;
    }

    @Override
    public String toString() {
        return "HomeModel_BannerData{" +
                "banner_id=" + banner_id +
                ", mobile_banner='" + mobile_banner + '\'' +
                ", heading='" + heading + '\'' +
                ", sub_heading='" + sub_heading + '\'' +
                '}';
    }
}
