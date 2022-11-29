package com.techive.mydailygoodscustomer.Models;

import com.google.gson.annotations.SerializedName;

public class CityList_Data {

    @SerializedName("id")
    private int id;

    @SerializedName("state_id")
    private String state_id;

    @SerializedName("city")
    private String city;

    public CityList_Data(int id, String state_id, String city) {
        this.id = id;
        this.state_id = state_id;
        this.city = city;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getState_id() {
        return state_id;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "CityList_Data{" +
                "id='" + id + '\'' +
                ", state_id='" + state_id + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
