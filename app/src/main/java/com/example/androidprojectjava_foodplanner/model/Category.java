package com.example.androidprojectjava_foodplanner.model;

import com.google.gson.annotations.SerializedName;

public class Category {
    @SerializedName("idCategory")
    private int id;
    @SerializedName("strCategory")
    private String name;
    @SerializedName("strCategoryThumb")
    private String imageUrl;
    @SerializedName("strCategoryDescription")
    private String description;

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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
