package com.example.androidprojectjava_foodplanner.AppNavigationView.profile.view;

public interface ProfileContract {
    void init(String name);
    void showEditNameDial();
    void showLogoutConfirm();
    void openFavouriteMeals();
    void openPlannedMeals();
    void goToLoginHome();
}
