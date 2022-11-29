package com.techive.mydailygoodscustomer.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderHistory {

    /*{
    "msg": "Success",
    "data": [
        {
            "id": 43,
            "invoiceId": "mdg_1261653892605",
            "b_id": "126",
            "vender_id": "134",
            "total": 219,
            "quantity": 2,
            "total_saving": 19,
            "delivery_change": 0,
            "discount": 19,
            "coupon_id": "36",
            "freebie_id": null,
            "self_pickup": 0,
            "buy_addressId": null,
            "order_status": "0",
            "online_payment": "0",
            "status": null,
            "message": null,
            "delivery_date": null,
            "created_at": "2022-05-30 06:36:45",
            "updated_at": null,
            "store_name": "Ashwani Store"
            "image": "1580_PANIPURI.jpg",
            "imagepath": "https://www.mydailygoods.com/admin/product_img/",
            "products": [
                {
                    "orderID": "mdg_1261653892605",
                    "qty": 1,
                    "new_qty": 1,
                    "sale_price": 55,
                    "totalPrice": 55,
                    "name": "PANIPURI Masala",
                    "image": "1580_PANIPURI.jpg",
                    "product_id": 32479
                },
                {
                    "orderID": "mdg_1261653892605",
                    "qty": 1,
                    "new_qty": 1,
                    "sale_price": 133,
                    "totalPrice": 133,
                    "name": "Tata Sampann 100% Organic Toor Dal",
                    "image": "1616_toordal.jpg",
                    "product_id": 32515
                }
            ],
            "buyerAddress": null
        }
    ],
    "error": 200
}*/

    @SerializedName("msg")
    private String msg;

    @SerializedName("data")
    private List<OrderHistory_Data> data;

    @SerializedName("error")
    private int error;

    public OrderHistory(String msg, List<OrderHistory_Data> data, int error) {
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

    public List<OrderHistory_Data> getData() {
        return data;
    }

    public void setData(List<OrderHistory_Data> data) {
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
        return "OrderHistory{" +
                "msg='" + msg + '\'' +
                ", data=" + data +
                ", error=" + error +
                '}';
    }
}
