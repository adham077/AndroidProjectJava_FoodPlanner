package com.example.androidprojectjava_foodplanner.model.repository;

import com.example.androidprojectjava_foodplanner.model.pojo.PlannedMeal;

import java.util.List;

public interface PlannedMealsDBCallBack {
    public void onTaskCompleted(List<PlannedMeal> meals);
}
