package com.example.androidprojectjava_foodplanner.AppNavigationView.navigationActivity.view;

public interface FragComm {
    void showLoading();
    void showHome();
    void popBack();
    void showProfile();
    void showFavourite();
    void showGuestProfile();
    void showCalendar();
    void showSearch();

    void showDetailsActivity(int MealId,String sender);
}
