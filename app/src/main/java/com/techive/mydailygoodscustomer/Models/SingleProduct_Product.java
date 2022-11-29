package com.techive.mydailygoodscustomer.Models;

import com.google.gson.annotations.SerializedName;

public class SingleProduct_Product {

    /*"products": {
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
        "ordered": 0
        "rating_average": 5
    }*/

    // id is actually the product Id (as per old database)
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

    @SerializedName("status")
    private int status;

    @SerializedName("cat_id")
    private String cat_id;

    @SerializedName("sub_cat_id")
    private String sub_cat_id;

    @SerializedName("short_desc")
    private String short_desc;

    @SerializedName("long_desc")
    private String long_desc;

    @SerializedName("mrp_price")
    private float mrp_price;

    @SerializedName("sale_price")
    private float sale_price;

    @SerializedName("prodct_cd_no")
    private long prodct_cd_no;

    @SerializedName("stock_status")
    private int stock_status;

    @SerializedName("cur_stock")
    private int cur_stock;

    @SerializedName("automate")
    private int automate;

    @SerializedName("weight")
    private String weight;

    @SerializedName("weight_with_packaging")
    private String weight_with_packaging;

    @SerializedName("tags")
    private String tags;

    @SerializedName("keywords")
    private String keywords;

    @SerializedName("brand_id")
    private int brand_id;

    @SerializedName("ordered")
    private int ordered;

    @SerializedName("rating_average")
    private Float rating_average;

    public SingleProduct_Product(int id, int adminOrVenderId, String name, String image, String image2, String image3, String image4, String image5, int status, String cat_id, String sub_cat_id, String short_desc, String long_desc, float mrp_price, float sale_price, long product_cd_no, int stock_status, int cur_stock, int automate, String weight, String weight_with_packaging, String tags, String keywords, int brand_id, int ordered, Float rating_average) {
        this.id = id;
        this.adminOrVenderId = adminOrVenderId;
        this.name = name;
        this.image = image;
        this.image2 = image2;
        this.image3 = image3;
        this.image4 = image4;
        this.image5 = image5;
        this.status = status;
        this.cat_id = cat_id;
        this.sub_cat_id = sub_cat_id;
        this.short_desc = short_desc;
        this.long_desc = long_desc;
        this.mrp_price = mrp_price;
        this.sale_price = sale_price;
        this.prodct_cd_no = product_cd_no;
        this.stock_status = stock_status;
        this.cur_stock = cur_stock;
        this.automate = automate;
        this.weight = weight;
        this.weight_with_packaging = weight_with_packaging;
        this.tags = tags;
        this.keywords = keywords;
        this.brand_id = brand_id;
        this.ordered = ordered;
        this.rating_average = rating_average;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getSub_cat_id() {
        return sub_cat_id;
    }

    public void setSub_cat_id(String sub_cat_id) {
        this.sub_cat_id = sub_cat_id;
    }

    public String getShort_desc() {
        return short_desc;
    }

    public void setShort_desc(String short_desc) {
        this.short_desc = short_desc;
    }

    public String getLong_desc() {
        return long_desc;
    }

    public void setLong_desc(String long_desc) {
        this.long_desc = long_desc;
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

    public long getProdct_cd_no() {
        return prodct_cd_no;
    }

    public void setProdct_cd_no(long prodct_cd_no) {
        this.prodct_cd_no = prodct_cd_no;
    }

    public int getStock_status() {
        return stock_status;
    }

    public void setStock_status(int stock_status) {
        this.stock_status = stock_status;
    }

    public int getCur_stock() {
        return cur_stock;
    }

    public void setCur_stock(int cur_stock) {
        this.cur_stock = cur_stock;
    }

    public int getAutomate() {
        return automate;
    }

    public void setAutomate(int automate) {
        this.automate = automate;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getWeight_with_packaging() {
        return weight_with_packaging;
    }

    public void setWeight_with_packaging(String weight_with_packaging) {
        this.weight_with_packaging = weight_with_packaging;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public int getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(int brand_id) {
        this.brand_id = brand_id;
    }

    public int getOrdered() {
        return ordered;
    }

    public void setOrdered(int ordered) {
        this.ordered = ordered;
    }

    public Float getRating_average() {
        return rating_average;
    }

    public void setRating_average(Float rating_average) {
        this.rating_average = rating_average;
    }

    @Override
    public String toString() {
        return "SingleProduct_Product{" +
                "id=" + id +
                ", adminOrVenderId=" + adminOrVenderId +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", image2='" + image2 + '\'' +
                ", image3='" + image3 + '\'' +
                ", image4='" + image4 + '\'' +
                ", image5='" + image5 + '\'' +
                ", status=" + status +
                ", cat_id='" + cat_id + '\'' +
                ", sub_cat_id='" + sub_cat_id + '\'' +
                ", short_desc='" + short_desc + '\'' +
                ", long_desc='" + long_desc + '\'' +
                ", mrp_price=" + mrp_price +
                ", sale_price=" + sale_price +
                ", prodct_cd_no=" + prodct_cd_no +
                ", stock_status=" + stock_status +
                ", cur_stock=" + cur_stock +
                ", automate=" + automate +
                ", weight='" + weight + '\'' +
                ", weight_with_packaging='" + weight_with_packaging + '\'' +
                ", tags='" + tags + '\'' +
                ", keywords='" + keywords + '\'' +
                ", brand_id=" + brand_id +
                ", ordered=" + ordered +
                ", rating_average=" + rating_average +
                '}';
    }
}
