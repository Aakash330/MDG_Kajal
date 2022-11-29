package com.techive.mydailygoodscustomer.Models;

import com.google.gson.annotations.SerializedName;

public class HomeModel_CategoryData {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("image")
    private String image;

    public HomeModel_CategoryData(int id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
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

    @Override
    public String toString() {
        return "HomeModel_CategoryData{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
