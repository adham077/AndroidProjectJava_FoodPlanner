package com.example.androidprojectjava_foodplanner.remote;

import com.example.androidprojectjava_foodplanner.model.Category;

import java.util.List;

public interface CategoriesNetworkCB {
    public void onSuccess(List<Category> categories);
    public void onFailure(String msg);
}
