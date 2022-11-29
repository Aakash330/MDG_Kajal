package com.techive.mydailygoodscustomer.Models;

import com.google.gson.annotations.SerializedName;

public class SearchProducts_Products_Data {

    /*MIGHT USE THE COMMENTED OUT VARIABLES IN FUTURE.*/

    @SerializedName("id")
    private int id;

    @SerializedName("adminOrVenderId")
    private int adminOrVenderId;

    @SerializedName("name")
    private String name;

    @SerializedName("image")
    private String image;

    @SerializedName("image2")
    private String image2;

    @SerializedName("image3")
    private String image3;

    @SerializedName("image4")
    private String image4;

    @SerializedName("image5")
    private String image5;

//    private int status;

//    private String cat_id;

//    private String sub_cat_id;

//    private String short_desc;

//    private String long_desc;

    @SerializedName("mrp_price")
    private float mrp_price;

    @SerializedName("sale_price")
    private float sale_price;

//    private long product_cd_no;

//    private int stock_status;

//    private int cur_stock;

//    private int automate;

    @SerializedName("weight")
    private String weight;

//    private String weight_with_packaging;

//    private String tags;

//    private String keywords;

//    private int brand_id;

    public SearchProducts_Products_Data(int id, int adminOrVenderId, String name, String image, String image2, String image3, String image4, String image5, float mrp_price, float sale_price, String weight) {
        this.id = id;
        this.adminOrVenderId = adminOrVenderId;
        this.name = name;
        this.image = image;
        this.image2 = image2;
        this.image3 = image3;
        this.image4 = image4;
        this.image5 = image5;
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

    public int getAdminOrVenderId() {
        return adminOrVenderId;
    }

    public void setAdminOrVenderId(int adminOrVenderId) {
        this.adminOrVenderId = adminOrVenderId;
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

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getImage4() {
        return image4;
    }

    public void setImage4(String image4) {
        this.image4 = image4;
    }

    public String getImage5() {
        return image5;
    }

    public void setImage5(String image5) {
        this.image5 = image5;
    }

    public float getMrp_price() {
        return mrp_price;
    }

    public void setMrp_price(float mrp_price) {
        this.mrp_price = mrp_price;
    }

    public float getSale_price() {
        return sale_price;
    }

    public void setSale_price(float sale_price) {
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
        return "SearchProducts_Products_Data{" +
                "id=" + id +
                ", adminOrVenderId=" + adminOrVenderId +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", image2='" + image2 + '\'' +
                ", image3='" + image3 + '\'' +
                ", image4='" + image4 + '\'' +
                ", image5='" + image5 + '\'' +
                ", mrp_price=" + mrp_price +
                ", sale_price=" + sale_price +
                ", weight='" + weight + '\'' +
                '}';
    }
}
