package com.example.androidprojectjava_foodplanner.AppNavigationView.calendar.presenter;

import android.graphics.Bitmap;

import com.example.androidprojectjava_foodplanner.AppNavigationView.calendar.view.CalendarContract;
import com.example.androidprojectjava_foodplanner.local.database.PlannedMealCB;
import com.example.androidprojectjava_foodplanner.model.pojo.Meal;
import com.example.androidprojectjava_foodplanner.model.pojo.PlannedMeal;
import com.example.androidprojectjava_foodplanner.model.repository.MealRepository;
import com.example.androidprojectjava_foodplanner.model.repository.UserRepository;
import com.example.androidprojectjava_foodplanner.remote.meal.ingredients.IngredientsRemoteDataSource;

import java.util.ArrayList;
import java.util.List;

public class CalendarPresenter {
    private static CalendarPresenter instance = null;
    private CalendarContract view;
    private MealRepository mealRepository;
    private UserRepository userRepository;
    private CalendarPresenter(UserRepository userRepository,MealRepository mealRepository, CalendarContract view){
        this.userRepository = userRepository;
        this.view = view;
        this.mealRepository = mealRepository;
    }

    public static CalendarPresenter getInstance(UserRepository userRepository,MealRepository mealRepository,CalendarContract view){
        if (instance == null){
            instance = new CalendarPresenter(userRepository,mealRepository,view);
        }
        return instance;
    }

    public void onDateChanged(int day,int month,int year){
        mealRepository.getPlannedMealByDateLocal(day, month, year, new PlannedMealCB() {
            @Override
            public void found(PlannedMeal plannedMeal) {
                if(plannedMeal != null){
                    getPlannedMealData(plannedMeal);
                }
                else{
                    view.showNoMealView();
                }
            }
        });
    }

    public void getPlannedMealData(PlannedMeal plannedMeal){
        Meal meal = plannedMeal.getMeal();
        List<Meal> _meal = new ArrayList<>();
        _meal.add(meal);
        userRepository.getMealSavedImages(_meal, view.provideContext(), new UserRepository.MealImagesCB() {
            @Override
            public void onSuccess(List<Bitmap> imagesBitmap) {
                Bitmap imageBitmap = imagesBitmap.get(0);
                view.showMealView(meal,imageBitmap);
                view.setMealDate(plannedMeal.getDay(),plannedMeal.getMonth(),plannedMeal.getYear());
            }

            @Override
            public void onFailure(String message) {
                view.showNoMealView();
            }
        });
    }


}
