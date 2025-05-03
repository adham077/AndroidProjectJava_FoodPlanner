package com.example.androidprojectjava_foodplanner.AppNavigationView.favourite.presenter;

import android.graphics.Bitmap;

import com.example.androidprojectjava_foodplanner.AppNavigationView.favourite.view.FavContract;
import com.example.androidprojectjava_foodplanner.model.pojo.FavouriteMeal;
import com.example.androidprojectjava_foodplanner.model.pojo.Meal;
import com.example.androidprojectjava_foodplanner.model.repository.FavouriteMealsDBCallBack;
import com.example.androidprojectjava_foodplanner.model.repository.MealRepository;
import com.example.androidprojectjava_foodplanner.model.repository.OperationCB;
import com.example.androidprojectjava_foodplanner.model.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class FavPresenter {
    private static FavPresenter instance = null;
    private UserRepository userRepository;
    private FavContract view;
    private MealRepository mealRepository;

    private FavPresenter(UserRepository userRepository, MealRepository mealRepository, FavContract view) {
        this.userRepository = userRepository;
        this.mealRepository = mealRepository;
        this.view = view;
    }

    public static FavPresenter getInstance(UserRepository userRepository,MealRepository mealRepository ,FavContract view) {
        if (instance == null) {
            instance = new FavPresenter(userRepository,mealRepository,view);
        }
        return instance;
    }

    public void onDeleteClicked(Meal meal){
        userRepository.removeFavouriteMeal(new FavouriteMeal(meal), new OperationCB() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(int errorID) {

            }
        });
    }

    public void init(){
        mealRepository.getAllFavouriteMeals(new FavouriteMealsDBCallBack() {
            @Override
            public void onTaskCompleted(List<FavouriteMeal> meals) {
                List<Meal>_meal = new ArrayList<>();
                _meal.addAll(meals);
                userRepository.getMealSavedImages(_meal, view.provideFavContext(), new UserRepository.MealImagesCB() {
                    @Override
                    public void onSuccess(List<Bitmap> imagesBitmap) {
                        view.showMeals(_meal,imagesBitmap);
                    }
                    @Override
                    public void onFailure(String message) {

                    }
                });
            }
        });
    }


}
