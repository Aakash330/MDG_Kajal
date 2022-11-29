package com.techive.mydailygoodscustomer.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductsModel {

    @SerializedName("msg")
    private String msg;

    @SerializedName("data")
    private List<ProductsModel_Data> data;

    @SerializedName("current_page")
    private int current_page;

    @SerializedName("last_page")
    private int last_page;

    @SerializedName("error")
    private int error;

    public ProductsModel(String msg, List<ProductsModel_Data> data, int current_page, int last_page, int error) {
        this.msg = msg;
        this.data = data;
        this.current_page = current_page;
        this.last_page = last_page;
        this.error = error;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<ProductsModel_Data> getData() {
        return data;
    }

    public void setData(List<ProductsModel_Data> data) {
        this.data = data;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public int getLast_page() {
        return last_page;
    }

    public void setLast_page(int last_page) {
        this.last_page = last_page;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "ProductsModel{" +
                "msg='" + msg + '\'' +
                ", data=" + data +
                ", current_page=" + current_page +
                ", last_page=" + last_page +
                ", error=" + error +
                '}';
    }
}
