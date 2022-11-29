package com.techive.mydailygoodscustomer.Models;

import com.google.gson.annotations.SerializedName;

public class StateList_Data {

    @SerializedName("id")
    private int id;

    @SerializedName("state")
    private String state;

    public StateList_Data(int id, String state) {
        this.id = id;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "StateList_Data{" +
                "id=" + id +
                ", state='" + state + '\'' +
                '}';
    }

    /*"data": [
    {
        "id": 1,
            "state": "Andaman & Nicobar Islands"
    },*/
}
