package com.techive.mydailygoodscustomer.Models;

import com.google.gson.annotations.SerializedName;

public class ProductOwnRating_Data {

    /*"data": {
        "star": 4,
        "text": "good"
    }*/

    @SerializedName("star")
    private float star;

    @SerializedName("text")
    private String text;

    public ProductOwnRating_Data(float star, String text) {
        this.star = star;
        this.text = text;
    }

    public float getStar() {
        return star;
    }

    public void setStar(float star) {
        this.star = star;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "ProductOwnRating_Data{" +
                "star=" + star +
                ", text='" + text + '\'' +
                '}';
    }
}
