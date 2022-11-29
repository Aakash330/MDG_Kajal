package com.techive.mydailygoodscustomer.Models;

import com.google.gson.annotations.SerializedName;

public class Cart_CartData_Data {

    /* "data": [
            {
                "cart_id": 110,
                "prod_id": 21162,
                "name": "Bingo! Original Style Salt Sprinkled Potato Chips ",
                "image": "https://www.mydailygoods.com/admin/product_img/1626_YMTFLCUTSALRS20.jpg",
                "qty": 4,
                "weight": "250 pcs",
                "totalPrice": "80",
                "saving": "0"
            }
        ],*/

    @SerializedName("cart_id")
    private int cart_id;

    @SerializedName("prod_id")
    private int prod_id;

    @SerializedName("name")
    private String name;

    @SerializedName("image")
    private String image;

    @SerializedName("qty")
    private int qty;

    @SerializedName("weight")
    private String weight;

    @SerializedName("totalPrice")
    private String totalPrice;

    @SerializedName("saving")
    private String saving;

    public Cart_CartData_Data(int cart_id, int prod_id, String name, String image, int qty, String weight, String totalPrice, String saving) {
        this.cart_id = cart_id;
        this.prod_id = prod_id;
        this.name = name;
        this.image = image;
        this.qty = qty;
        this.weight = weight;
        this.totalPrice = totalPrice;
        this.saving = saving;
    }

    public int getCart_id() {
        return cart_id;
    }

    public void setCart_id(int cart_id) {
        this.cart_id = cart_id;
    }

    public int getProd_id() {
        return prod_id;
    }

    public void setProd_id(int prod_id) {
        this.prod_id = prod_id;
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

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getSaving() {
        return saving;
    }

    public void setSaving(String saving) {
        this.saving = saving;
    }

    @Override
    public String toString() {
        return "Cart_CartData_Data{" +
                "cart_id=" + cart_id +
                ", prod_id=" + prod_id +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", qty=" + qty +
                ", weight='" + weight + '\'' +
                ", totalPrice='" + totalPrice + '\'' +
                ", saving='" + saving + '\'' +
                '}';
    }
}
