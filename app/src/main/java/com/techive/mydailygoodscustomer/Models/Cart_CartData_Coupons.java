package com.techive.mydailygoodscustomer.Models;

import com.google.gson.annotations.SerializedName;

public class Cart_CartData_Coupons {

    /* "coupons": [
            {
                "id": 30,
                "coupon": "10 % discount | RG68ZU2Z",
                "min_spend": 200,
                "max_discount": 0,
                "limit_per_user": 1,
                "first_run": 0,
                "monthly_run": 0
            }
        ],*/

    /*{
                "id": 33,
                "vendor_id": 121,
                "name": "300 Off on 500",
                "description": "Get Rs 300 off on min spend of Rs 500.",
                "type": "fcd",
                "ammount": 300,
                "min_spend": 500,
                "max_discount": 0,
                "limit_per_user": 100,
                "start_date": "2022-05-01",
                "expiry_date": "2022-05-31",
                "product_cat": "7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28",
                "first_run": 0,
                "monthly": 1,
                "created_at": "2022-05-19 10:48:34",
                "updated_at": "2022-05-19 10:48:34"
            }*/


    @SerializedName("id")
    private int id;

//    @SerializedName("coupon")
//    private String coupon;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("type")
    private String type;

    @SerializedName("ammount")
    private int ammount;

    @SerializedName("min_spend")
    private int min_spend;

    @SerializedName("max_discount")
    private int max_discount;

    @SerializedName("status")
    private String status;

    @SerializedName("msg")
    private String msg;

    @SerializedName("limit_per_user")
    private int limit_per_user;

    @SerializedName("first_run")
    private int first_run;

    @SerializedName("monthly_run")
    private int monthly_run;

    public Cart_CartData_Coupons(int id, String name, String description, String type, int ammount, int min_spend, int max_discount, String status, String msg, int limit_per_user, int first_run, int monthly_run) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.ammount = ammount;
        this.min_spend = min_spend;
        this.max_discount = max_discount;
        this.status = status;
        this.msg = msg;
        this.limit_per_user = limit_per_user;
        this.first_run = first_run;
        this.monthly_run = monthly_run;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getAmmount() {
        return ammount;
    }

    public void setAmmount(int ammount) {
        this.ammount = ammount;
    }

    public int getMin_spend() {
        return min_spend;
    }

    public void setMin_spend(int min_spend) {
        this.min_spend = min_spend;
    }

    public int getMax_discount() {
        return max_discount;
    }

    public void setMax_discount(int max_discount) {
        this.max_discount = max_discount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getLimit_per_user() {
        return limit_per_user;
    }

    public void setLimit_per_user(int limit_per_user) {
        this.limit_per_user = limit_per_user;
    }

    public int getFirst_run() {
        return first_run;
    }

    public void setFirst_run(int first_run) {
        this.first_run = first_run;
    }

    public int getMonthly_run() {
        return monthly_run;
    }

    public void setMonthly_run(int monthly_run) {
        this.monthly_run = monthly_run;
    }

    @Override
    public String toString() {
        return "Cart_CartData_Coupons{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", type='" + type + '\'' +
                ", ammount=" + ammount +
                ", min_spend=" + min_spend +
                ", max_discount=" + max_discount +
                ", status='" + status + '\'' +
                ", msg='" + msg + '\'' +
                ", limit_per_user=" + limit_per_user +
                ", first_run=" + first_run +
                ", monthly_run=" + monthly_run +
                '}';
    }
}
