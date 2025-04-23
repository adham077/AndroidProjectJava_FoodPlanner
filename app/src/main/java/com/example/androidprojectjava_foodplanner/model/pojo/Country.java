package com.example.androidprojectjava_foodplanner.model.pojo;

import com.google.gson.annotations.SerializedName;

public class Country {
    @SerializedName("strArea")
    private String name;
    private transient int id;
    private transient int imageId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
