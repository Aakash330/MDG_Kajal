package com.techive.mydailygoodscustomer.Models;

import com.google.gson.annotations.SerializedName;

public class Cart {

    /*{
    "msg": "success",
    "error": 200,
    "cart_data": {
        "current_page": 1,
        "data": [
            {
                "cart_id": 110,
                "prod_id": 21162,
                "name": "Bingo! Original Style Salt Sprinkled Potato Chips ",
                "image": "https://www.mydailygoods.com/admin/product_img/1626_YMTFLCUTSALRS20.jpg",
                "qty": 4,
                "totalPrice": "80",
                "saving": "0"
            }
        ],
        "coupons": [
            {
                "id": 30,
                "coupon": "10 % discount | RG68ZU2Z",
                "min_spend": 200,
                "max_discount": 0,
                "limit_per_user": 1,
                "first_run": 0,
                "monthly_run": 0
            }
        ],
        "last_page": 1,
        "total": 1
    }
}*/

    @SerializedName("msg")
    private String msg;

    @SerializedName("error")
    private int error;

    @SerializedName("cart_data")
    private Cart_CartData cart_data;

    public Cart(String msg, int error, Cart_CartData cart_data) {
        this.msg = msg;
        this.error = error;
        this.cart_data = cart_data;
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

    public Cart_CartData getCart_data() {
        return cart_data;
    }

    public void setCart_data(Cart_CartData cart_data) {
        this.cart_data = cart_data;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "msg='" + msg + '\'' +
                ", error=" + error +
                ", cart_data=" + cart_data +
                '}';
    }
}
