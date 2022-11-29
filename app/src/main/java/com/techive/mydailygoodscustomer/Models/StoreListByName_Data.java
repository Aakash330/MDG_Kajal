package com.techive.mydailygoodscustomer.Models;

import com.google.gson.annotations.SerializedName;

public class StoreListByName_Data {

    @SerializedName("store_name")
    private String store_name;

    @SerializedName("store_id")
    private int store_id;

    public StoreListByName_Data(String store_name, int store_id) {
        this.store_name = store_name;
        this.store_id = store_id;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

    @Override
    public String toString() {
        return "StoreListByName_Data{" +
                "store_name='" + store_name + '\'' +
                ", store_id=" + store_id +
                '}';
    }
}
