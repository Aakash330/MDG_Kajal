package com.techive.mydailygoodscustomer.Models;

import com.google.gson.annotations.SerializedName;

public class BuyerAddAddressModel {

    /*buy_id:
fname:
lname:
mobile:
address:
landmark:
state_id:
city_id:
pin_code:*/

    @SerializedName("buy_id")
    private int buy_id;

    @SerializedName("addrsId")
    private int addrsId;

    @SerializedName("fname")
    private String fname;

    @SerializedName("lname")
    private String lname;

    @SerializedName("mobile")
    private String mobile;

    @SerializedName("address")
    private String address;

    @SerializedName("landmark")
    private String landmark;

    @SerializedName("state_id")
    private int state_id;

    @SerializedName("city_id")
    private int city_id;

    @SerializedName("pin_code")
    private String pin_code;

    public BuyerAddAddressModel(int buy_id, String fname, String lname, String mobile, String address, String landmark, int state_id, int city_id, String pin_code) {
        this.buy_id = buy_id;
        this.fname = fname;
        this.lname = lname;
        this.mobile = mobile;
        this.address = address;
        this.landmark = landmark;
        this.state_id = state_id;
        this.city_id = city_id;
        this.pin_code = pin_code;
    }

    public int getBuy_id() {
        return buy_id;
    }

    public void setBuy_id(int buy_id) {
        this.buy_id = buy_id;
    }

    public int getAddrsId() {
        return addrsId;
    }

    public void setAddrsId(int addrsId) {
        this.addrsId = addrsId;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public int getState_id() {
        return state_id;
    }

    public void setState_id(int state_id) {
        this.state_id = state_id;
    }

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public String getPin_code() {
        return pin_code;
    }

    public void setPin_code(String pin_code) {
        this.pin_code = pin_code;
    }

    @Override
    public String toString() {
        return "BuyerAddAddressModel{" +
                "buy_id=" + buy_id +
                ", addrsId=" + addrsId +
                ", fname='" + fname + '\'' +
                ", lname='" + lname + '\'' +
                ", mobile='" + mobile + '\'' +
                ", address='" + address + '\'' +
                ", landmark='" + landmark + '\'' +
                ", state_id=" + state_id +
                ", city_id=" + city_id +
                ", pin_code='" + pin_code + '\'' +
                '}';
    }
}
