package com.techive.mydailygoodscustomer.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchProducts_Products {

    @SerializedName("current_page")
    private int current_page;

    @SerializedName("data")
    private List<SearchProducts_Products_Data> data;

    @SerializedName("last_page")
    private int last_page;

    @SerializedName("total")
    private int total;

    public SearchProducts_Products(int current_page, List<SearchProducts_Products_Data> data, int last_page, int total) {
        this.current_page = current_page;
        this.data = data;
        this.last_page = last_page;
        this.total = total;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public List<SearchProducts_Products_Data> getData() {
        return data;
    }

    public void setData(List<SearchProducts_Products_Data> data) {
        this.data = data;
    }

    public int getLast_page() {
        return last_page;
    }

    public void setLast_page(int last_page) {
        this.last_page = last_page;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "SearchProducts_Products{" +
                "current_page=" + current_page +
                ", data=" + data +
                ", last_page=" + last_page +
                ", total=" + total +
                '}';
    }
}
