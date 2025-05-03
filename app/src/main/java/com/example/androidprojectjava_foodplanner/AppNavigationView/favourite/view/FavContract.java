package com.example.androidprojectjava_foodplanner.AppNavigationView.favourite.view;

import android.graphics.Bitmap;

import com.example.androidprojectjava_foodplanner.model.pojo.Meal;

import java.util.List;

public interface FavContract {
    void showMeals(List<Meal> mealList, List<Bitmap>mealImages);
    Object provideFavContext();
}
