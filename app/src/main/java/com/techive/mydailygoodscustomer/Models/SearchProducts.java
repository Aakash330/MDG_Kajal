package com.techive.mydailygoodscustomer.Models;

import com.google.gson.annotations.SerializedName;

public class SearchProducts {

    @SerializedName("msg")
    private String msg;

    @SerializedName("error")
    private int error;

    @SerializedName("products")
    private SearchProducts_Products products;

    public SearchProducts(String msg, int error, SearchProducts_Products products) {
        this.msg = msg;
        this.error = error;
        this.products = products;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public SearchProducts_Products getProducts() {
        return products;
    }

    public void setProducts(SearchProducts_Products products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "SearchProducts{" +
                "msg='" + msg + '\'' +
                ", error='" + error + '\'' +
                ", products=" + products +
                '}';
    }
}
