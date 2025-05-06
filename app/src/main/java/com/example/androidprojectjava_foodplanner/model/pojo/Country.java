package com.example.androidprojectjava_foodplanner.model.pojo;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

public class Country {
    @SerializedName("strArea")
    private String name;
    private transient int id;
    private transient int imageId;

    private transient Bitmap bitmap;

    public Country() {
    }

    public Country(String name, int id, int imageId, Bitmap bitmap) {
        this.name = name;
        this.id = id;
        this.imageId = imageId;
        this.bitmap = bitmap;
    }

    public Country(String name,int imageId) {
        this.name = name;
        this.imageId = imageId;
    }

    public Country(String name, Bitmap bitmap) {
        this.name = name;
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

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
