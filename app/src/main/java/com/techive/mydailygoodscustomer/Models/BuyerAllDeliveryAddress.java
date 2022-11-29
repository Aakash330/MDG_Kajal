package com.techive.mydailygoodscustomer.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BuyerAllDeliveryAddress {

    /*{
    "msg": "Success",
    "data": [
        {
            "id": 4,
            "u_id": 89,
            "name": "Ak",
            "lname": "gupta",
            "mobile": "7355761869",
            "address": "block c",
            "landmark": "gol market",
            "pincode": "201301",
            "status": 1,
            "state": "Uttar Pradesh",
            "city": "Meerut"
        },*/

    @SerializedName("msg")
    private String msg;

    @SerializedName("data")
    private List<Cart_CartData_BuyerAddress> data;

    @SerializedName("error")
    private int error;

    public BuyerAllDeliveryAddress(String msg, List<Cart_CartData_BuyerAddress> data, int error) {
        this.msg = msg;
        this.data = data;
        this.error = error;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<Cart_CartData_BuyerAddress> getData() {
        return data;
    }

    public void setData(List<Cart_CartData_BuyerAddress> data) {
        this.data = data;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "BuyerAllDeliveryAddress{" +
                "msg='" + msg + '\'' +
                ", data=" + data +
                ", error=" + error +
                '}';
    }
}
