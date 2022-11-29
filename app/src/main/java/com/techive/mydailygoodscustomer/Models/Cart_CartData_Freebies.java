package com.techive.mydailygoodscustomer.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Cart_CartData_Freebies {

    /* {
                "id": 7,
                "name": "test freebie 2-5-22",
                "description": "adding for products check",
                "min_cart_value": 670,
                "products": [
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
                ],
                "usage_limt_per_user": 3,
                "first_run": 1,
                "monthly": 0,
                "start_date": "2022-05-25",
                "end_date": "2022-05-28"
            },*/

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("min_cart_value")
    private int min_cart_value;

    @SerializedName("products")
    private List<Cart_CartData_Freebies_Products> products;

    public Cart_CartData_Freebies(int id, String name, String description, int min_cart_value, List<Cart_CartData_Freebies_Products> products) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.min_cart_value = min_cart_value;
        this.products = products;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMin_cart_value() {
        return min_cart_value;
    }

    public void setMin_cart_value(int min_cart_value) {
        this.min_cart_value = min_cart_value;
    }

    public List<Cart_CartData_Freebies_Products> getProducts() {
        return products;
    }

    public void setProducts(List<Cart_CartData_Freebies_Products> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "Cart_CartData_Freebies{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", min_cart_value=" + min_cart_value +
                ", products=" + products +
                '}';
    }
}
