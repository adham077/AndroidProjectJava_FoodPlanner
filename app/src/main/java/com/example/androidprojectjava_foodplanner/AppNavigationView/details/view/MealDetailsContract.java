package com.example.androidprojectjava_foodplanner.AppNavigationView.details.view;

import com.example.androidprojectjava_foodplanner.model.pojo.Ingredient;
import com.example.androidprojectjava_foodplanner.model.pojo.Meal;

import java.util.List;

public interface MealDetailsContract {

    enum FavCallBack{
        SUCCESS,
        FAILURE
    }
    void showLoading();
    void showMealDetails(Meal meal, List<Ingredient> ingredientList);
    void favCallBack(FavCallBack status);
    void failedToFetch();

    void highlightFav();
    void unhighlightFav();

    Object provideContext();
}
