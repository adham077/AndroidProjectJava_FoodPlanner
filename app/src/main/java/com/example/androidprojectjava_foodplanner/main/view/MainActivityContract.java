package com.example.androidprojectjava_foodplanner.main.view;

public interface MainActivityContract {
    enum UserState{
        LOGGED_IN,
        NOT_LOGGED_IN
    }
    void init(UserState state);
}
