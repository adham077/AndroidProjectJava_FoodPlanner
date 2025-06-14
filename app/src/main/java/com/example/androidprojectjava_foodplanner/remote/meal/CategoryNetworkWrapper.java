package com.example.androidprojectjava_foodplanner.remote.meal;

import com.example.androidprojectjava_foodplanner.model.pojo.Category;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryNetworkWrapper {
    @SerializedName("categories")
    private List<Category> categories;

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}
