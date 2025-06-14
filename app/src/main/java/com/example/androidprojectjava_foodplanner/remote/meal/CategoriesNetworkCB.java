package com.example.androidprojectjava_foodplanner.remote.meal;

import com.example.androidprojectjava_foodplanner.model.pojo.Category;

import java.util.List;

public interface CategoriesNetworkCB {
    public void onSuccess(List<Category> categories);
    public void onFailure(String msg);
}
