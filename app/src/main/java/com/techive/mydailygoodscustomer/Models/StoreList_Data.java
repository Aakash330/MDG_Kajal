package com.techive.mydailygoodscustomer.Models;

import com.google.gson.annotations.SerializedName;

public class StoreList_Data {

    @SerializedName("store_name")
    private String store_name;

    @SerializedName("address")
    private String address;

    @SerializedName("website")
    private String website;

    @SerializedName("mob_no1")
    private String mob_no1;

    @SerializedName("whap_no1")
    private String whap_no1;

    @SerializedName("cod")
    private String cod;

    @SerializedName("homedeliver")
    private String homedeliver;

    @SerializedName("image1")
    private String image1;

    @SerializedName("image2")
    private String image2;

    @SerializedName("image3")
    private String image3;

    @SerializedName("image4")
    private String image4;

    @SerializedName("image5")
    private String image5;

    //RATING & OPEN/CLOSE ARE LEFT


    public StoreList_Data(String store_name, String address, String website, String mob_no1, String whap_no1, String cod, String homedeliver, String image1, String image2, String image3, String image4, String image5) {
        this.store_name = store_name;
        this.address = address;
        this.website = website;
        this.mob_no1 = mob_no1;
        this.whap_no1 = whap_no1;
        this.cod = cod;
        this.homedeliver = homedeliver;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.image4 = image4;
        this.image5 = image5;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getMob_no1() {
        return mob_no1;
    }

    public void setMob_no1(String mob_no1) {
        this.mob_no1 = mob_no1;
    }

    public String getWhap_no1() {
        return whap_no1;
    }

    public void setWhap_no1(String whap_no1) {
        this.whap_no1 = whap_no1;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public String getHomedeliver() {
        return homedeliver;
    }

    public void setHomedeliver(String homedeliver) {
        this.homedeliver = homedeliver;
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

    public String getImage5() {
        return image5;
    }

    public void setImage5(String image5) {
        this.image5 = image5;
    }

    @Override
    public String toString() {
        return "StoreList_Data{" +
                "store_name='" + store_name + '\'' +
                ", address='" + address + '\'' +
                ", website='" + website + '\'' +
                ", mob_no1='" + mob_no1 + '\'' +
                ", whap_no1='" + whap_no1 + '\'' +
                ", cod='" + cod + '\'' +
                ", homedeliver='" + homedeliver + '\'' +
                ", image1='" + image1 + '\'' +
                ", image2='" + image2 + '\'' +
                ", image3='" + image3 + '\'' +
                ", image4='" + image4 + '\'' +
                ", image5='" + image5 + '\'' +
                '}';
    }
}
