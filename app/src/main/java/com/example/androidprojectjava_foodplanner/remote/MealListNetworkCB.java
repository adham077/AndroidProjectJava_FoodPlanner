package com.example.androidprojectjava_foodplanner.remote;

import com.example.androidprojectjava_foodplanner.model.Meal;

import java.util.List;

public interface MealListNetworkCB {
    public void onSuccess(List<Meal> meals);
    public void onFailure(String msg);
}
