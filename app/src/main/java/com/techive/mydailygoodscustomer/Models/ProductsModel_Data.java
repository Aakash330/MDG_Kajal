package com.techive.mydailygoodscustomer.Models;

import com.google.gson.annotations.SerializedName;

public class ProductsModel_Data {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("mrp_price")
    private String mrp_price;

    @SerializedName("sale_price")
    private String sale_price;

    @SerializedName("percent_discount")
    private int percent_discount;

    @SerializedName("weight")
    private String weight;

    @SerializedName("main_image")
    private String main_image;

    @SerializedName("image1")
    private String image1;

    @SerializedName("image2")
    private String image2;

    @SerializedName("image3")
    private String image3;

    @SerializedName("image4")
    private String image4;

    public ProductsModel_Data(int id, String name, String mrp_price, String sale_price, int percent_discount, String weight, String main_image, String image1, String image2, String image3, String image4) {
        this.id = id;
        this.name = name;
        this.mrp_price = mrp_price;
        this.sale_price = sale_price;
        this.percent_discount = percent_discount;
        this.weight = weight;
        this.main_image = main_image;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.image4 = image4;
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

    public int getPercent_discount() {
        return percent_discount;
    }

    public void setPercent_discount(int percent_discount) {
        this.percent_discount = percent_discount;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getMain_image() {
        return main_image;
    }

    public void setMain_image(String main_image) {
        this.main_image = main_image;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
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

    @Override
    public String toString() {
        return "ProductsModel_Data{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", mrp_price='" + mrp_price + '\'' +
                ", sale_price='" + sale_price + '\'' +
                ", percent_discount=" + percent_discount +
                ", weight='" + weight + '\'' +
                ", main_image='" + main_image + '\'' +
                ", image1='" + image1 + '\'' +
                ", image2='" + image2 + '\'' +
                ", image3='" + image3 + '\'' +
                ", image4='" + image4 + '\'' +
                '}';
    }
}
