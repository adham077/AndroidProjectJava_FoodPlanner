package com.example.androidprojectjava_foodplanner.remote.meal;

import com.example.androidprojectjava_foodplanner.model.Meal;

public interface MealNetworkCB {
    public void onSuccess(Meal meal);
    public void onFailure(String msg);
}
