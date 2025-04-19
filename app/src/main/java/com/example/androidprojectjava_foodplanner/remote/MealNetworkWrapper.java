package com.example.androidprojectjava_foodplanner.remote;

import com.example.androidprojectjava_foodplanner.model.Meal;

import java.util.List;

public class MealNetworkWrapper {
    private List<Meal> meals;

    public List<Meal> getMeals() {
        return meals;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }
}
