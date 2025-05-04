package com.example.androidprojectjava_foodplanner.model.pojo;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Ingredient implements Serializable {
    private String name;
    private String measurement;
    private Bitmap image;
    public Ingredient(String name, String measurement, Bitmap image) {
        this.name = name;
        this.measurement = measurement;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
