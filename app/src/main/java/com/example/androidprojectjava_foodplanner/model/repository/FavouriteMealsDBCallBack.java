package com.example.androidprojectjava_foodplanner.model.repository;

import com.example.androidprojectjava_foodplanner.model.pojo.FavouriteMeal;

import java.util.List;

public interface FavouriteMealsDBCallBack {
    void onTaskCompleted(List<FavouriteMeal> meals);
}
