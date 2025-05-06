package com.example.androidprojectjava_foodplanner.AppNavigationView.home.view;

import androidx.annotation.NonNull;

import com.example.androidprojectjava_foodplanner.model.pojo.Meal;

import java.util.List;

public interface HomeContract {

    void showOfflineView();
    void showLoadingView();
    void showOnlineView(@NonNull Meal randomMeal,@NonNull List<Meal> meals);

    void failedtoFetch();

    void showMealDetails(int mealID);
    public boolean isFragmentActive();

    Object getAppContext();
}
