package com.techive.mydailygoodscustomer.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HomeCart {

    /*{
    "msg": "Success",
    "data": [
        {
            "cart_id": 171,
            "prod_id": 37385,
            "name": "product 1",
            "image": "https://www.mydailygoods.com/admin/product_img/37385_JPEG_20220527_102909_8683604028500451162.jpg",
            "qty": 1,
            "weight": "2pcs",
            "totalPrice": "1",
            "saving": "0"
        }
    ],
    "error": 200
}*/

    @SerializedName("msg")
    private String msg;

    @SerializedName("data")
    private List<Cart_CartData_Data> data;

    @SerializedName("error")
    private int error;

    public HomeCart(String msg, List<Cart_CartData_Data> data, int error) {
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

    public List<Cart_CartData_Data> getData() {
        return data;
    }

    public void setData(List<Cart_CartData_Data> data) {
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
        return "HomeCart{" +
                "msg='" + msg + '\'' +
                ", data=" + data +
                ", error=" + error +
                '}';
    }
}
