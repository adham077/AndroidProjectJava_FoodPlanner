package com.example.androidprojectjava_foodplanner.AppNavigationView.calendar.view;

import android.graphics.Bitmap;

import com.example.androidprojectjava_foodplanner.model.pojo.Meal;

public interface CalendarContract {
    void showMealView(Meal meal, Bitmap imageBitmap);
    void showNoMealView();
    void setMealDate(int day,int month,int year);
    void failedOp(String message);
    Object provideContext();
}
