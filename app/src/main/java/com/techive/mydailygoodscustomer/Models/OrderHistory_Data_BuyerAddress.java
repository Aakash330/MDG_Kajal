package com.techive.mydailygoodscustomer.Models;

import com.google.gson.annotations.SerializedName;

public class OrderHistory_Data_BuyerAddress {

    /*buyerAddress":{"id":9,"firstname":"Bruce","lastname":"Wayne","mobile":"7896541230","address":"Street Address","landmark":"Landmark","pincode":"145698","state":"Delhi","city":"Delhi"}}],"error":200}*/

    @SerializedName("id")
    private int id;

    @SerializedName("firstname")
    private String firstname;

    @SerializedName("lastname")
    private String lastname;

    @SerializedName("mobile")
    private String mobile;

    @SerializedName("address")
    private String address;

    @SerializedName("landmark")
    private String landmark;

    @SerializedName("pincode")
    private String pincode;

    @SerializedName("state")
    private String state;

    @SerializedName("city")
    private String city;

    public OrderHistory_Data_BuyerAddress(int id, String firstname, String lastname, String mobile, String address, String landmark, String pincode, String state, String city) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.mobile = mobile;
        this.address = address;
        this.landmark = landmark;
        this.pincode = pincode;
        this.state = state;
        this.city = city;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
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

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
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

    @Override
    public String toString() {
        return "OrderHistory_Data_BuyerAddress{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", mobile='" + mobile + '\'' +
                ", address='" + address + '\'' +
                ", landmark='" + landmark + '\'' +
                ", pincode='" + pincode + '\'' +
                ", state='" + state + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
