package com.example.androidprojectjava_foodplanner.remote.meal;

import com.example.androidprojectjava_foodplanner.model.pojo.Meal;

import java.util.List;

public interface MealListNetworkCB {
    public void onSuccess(List<Meal> meals);
    public void onFailure(String msg);
}
