package com.techive.mydailygoodscustomer.Models;

import com.google.gson.annotations.SerializedName;

public class SingleProduct {

    /*{
    "msg": "success",
    "error": 200,
    "products": {
        "id": 39596,
        "adminOrVenderId": 134,
        "name": "Dabur Badam Oil",
        "image": "https://www.mydailygoods.com/admin/product_img/258_Dabur-Badam-Oil.jpg",
        "image2": "",
        "image3": "",
        "image4": "",
        "image5": "",
        "status": 1,
        "cat_id": "13,14",
        "sub_cat_id": "0",
        "short_desc": null,
        "long_desc": null,
        "mrp_price": 425,
        "sale_price": 425,
        "prodct_cd_no": null,
        "stock_status": 1,
        "cur_stock": 1,
        "automate": 0,
        "weight": "100 ml",
        "weight_with_packaging": null,
        "tags": null,
        "keywords": "Baby Care",
        "brand_id": 75
    }
}*/

    @SerializedName("msg")
    private String msg;

    @SerializedName("error")
    private int error;

    @SerializedName("products")
    private SingleProduct_Product products;

    public SingleProduct(String msg, int error, SingleProduct_Product products) {
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

    public SingleProduct_Product getProducts() {
        return products;
    }

    public void setProducts(SingleProduct_Product products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "SingleProduct{" +
                "msg='" + msg + '\'' +
                ", error=" + error +
                ", products=" + products +
                '}';
    }
}
