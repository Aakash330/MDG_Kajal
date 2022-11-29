package com.techive.mydailygoodscustomer.Models;

import com.google.gson.annotations.SerializedName;

public class Cart_CartData_Freebies_Products {

    /*"products": [
                    {
                        "id": 24432,
                        "name": "All Season's Super Rice",
                        "image": "https://www.mydailygoods.com/admin/product_img/26_All-Seasons-Super-Rice.jpg",
                        "mrp_price": "170.00",
                        "sale_price": "170.00",
                        "weight": "1 Kg"
                    },
                    {
                        "id": 24536,
                        "name": "Select Basmati Rice",
                        "image": "https://www.mydailygoods.com/admin/product_img/130_Basmati-Rice.jpeg",
                        "mrp_price": "85.00",
                        "sale_price": "85.00",
                        "weight": "1 Kg"
                    }
                ],*/

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("image")
    private String image;

    @SerializedName("mrp_price")
    private String mrp_price;

    @SerializedName("sale_price")
    private String sale_price;

    @SerializedName("weight")
    private String weight;

    public Cart_CartData_Freebies_Products(int id, String name, String image, String mrp_price, String sale_price, String weight) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.mrp_price = mrp_price;
        this.sale_price = sale_price;
        this.weight = weight;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getMrp_price() {
        return mrp_price;
    }

    public void setMrp_price(String mrp_price) {
        this.mrp_price = mrp_price;
    }

    public String getSale_price() {
        return sale_price;
    }

    public void setSale_price(String sale_price) {
        this.sale_price = sale_price;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Cart_CartData_Freebies_Products{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", mrp_price='" + mrp_price + '\'' +
                ", sale_price='" + sale_price + '\'' +
                ", weight='" + weight + '\'' +
                '}';
    }
}
