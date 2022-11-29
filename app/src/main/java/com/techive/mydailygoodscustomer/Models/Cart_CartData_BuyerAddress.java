package com.techive.mydailygoodscustomer.Models;

import com.google.gson.annotations.SerializedName;

public class Cart_CartData_BuyerAddress {

     /*"buyerDefaultAddress": {
            "id": 8,
            "u_id": 126,
            "name": "Aamir",
            "lname": "Hoda",
            "mobile": "7988821805",
            "address": "Alamganj, Ashok Rajpath, Patna, Bihar",
            "landmark": null,
            "pincode": "800007",
            "state": "Bihar",
            "city": "Patna"
            "state_id": 5,
            "city_id": 394
        }*/

    @SerializedName("id")
    private int id;

    @SerializedName("u_id")
    private int u_id;

    @SerializedName("name")
    private String name;

    @SerializedName("lname")
    private String lname;

    @SerializedName("mobile")
    private String mobile;

    @SerializedName("address")
    private String address;

    @SerializedName("landmark")
    private String landmark;

    @SerializedName("pincode")
    private String pincode;

    /*NEEDED ONLY BY LIST OF BUYER DELIVERY ADDRESSES API*/
    @SerializedName("status")
    private int status;

    @SerializedName("state")
    private String state;

    @SerializedName("city")
    private String city;

    @SerializedName("state_id")
    private int state_id;

    @SerializedName("city_id")
    private int city_id;

    public Cart_CartData_BuyerAddress(int id, int u_id, String name, String lname, String mobile, String address, String landmark, String pincode, int status, String state, String city, int state_id, int city_id) {
        this.id = id;
        this.u_id = u_id;
        this.name = name;
        this.lname = lname;
        this.mobile = mobile;
        this.address = address;
        this.landmark = landmark;
        this.pincode = pincode;
        this.status = status;
        this.state = state;
        this.city = city;
        this.state_id = state_id;
        this.city_id = city_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getU_id() {
        return u_id;
    }

    public void setU_id(int u_id) {
        this.u_id = u_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    @Override
    public String toString() {
        return "Cart_CartData_BuyerAddress{" +
                "id=" + id +
                ", u_id=" + u_id +
                ", name='" + name + '\'' +
                ", lname='" + lname + '\'' +
                ", mobile='" + mobile + '\'' +
                ", address='" + address + '\'' +
                ", landmark='" + landmark + '\'' +
                ", pincode='" + pincode + '\'' +
                ", status=" + status +
                ", state='" + state + '\'' +
                ", city='" + city + '\'' +
                ", state_id=" + state_id +
                ", city_id=" + city_id +
                '}';
    }
}
