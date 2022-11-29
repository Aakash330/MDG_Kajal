package com.techive.mydailygoodscustomer.Models;

import com.google.gson.annotations.SerializedName;

public class OrderHistory_Data_Products {

    /* "products": [
                {
                    "orderID": "mdg_1261653892605",
                    "qty": 1,
                    "new_qty": 1,
                    "sale_price": 55,
                    "totalPrice": 55,
                    "name": "PANIPURI Masala",
                    "image": "1580_PANIPURI.jpg",
                    "product_id": 32479
                },*/

    @SerializedName("orderID")
    private String orderID;

    @SerializedName("qty")
    private int qty;

    @SerializedName("new_qty")
    private int new_qty;

    @SerializedName("sale_price")
    private float sale_price;

    @SerializedName("totalPrice")
    private float totalPrice;

    @SerializedName("name")
    private String name;

    @SerializedName("image")
    private String image;

    @SerializedName("product_id")
    private int product_id;

    public OrderHistory_Data_Products(String orderID, int qty, int new_qty, float sale_price, float totalPrice, String name, String image, int product_id) {
        this.orderID = orderID;
        this.qty = qty;
        this.new_qty = new_qty;
        this.sale_price = sale_price;
        this.totalPrice = totalPrice;
        this.name = name;
        this.image = image;
        this.product_id = product_id;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getNew_qty() {
        return new_qty;
    }

    public void setNew_qty(int new_qty) {
        this.new_qty = new_qty;
    }

    public float getSale_price() {
        return sale_price;
    }

    public void setSale_price(float sale_price) {
        this.sale_price = sale_price;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    @Override
    public String toString() {
        return "OrderHistory_Data_Products{" +
                "orderID='" + orderID + '\'' +
                ", qty=" + qty +
                ", new_qty=" + new_qty +
                ", sale_price=" + sale_price +
                ", totalPrice=" + totalPrice +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", product_id=" + product_id +
                '}';
    }
}
