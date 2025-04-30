package com.example.androidprojectjava_foodplanner.model.repository;

public interface OperationCB {
    int SUCCESSFUL_LOGIN = 0;
    int INVALID_EMAIL_OR_PASSWORD = 1;
    int INVALID_USER = 2;
    int NETWORK_ERROR = 3;
    int AuthentiCAtION_FAILED = 4;

    int USER_NOT_LOGGED_IN = 5;

    int DUPLICTE_FAVOURITE_MEAL = 6;

    int DUPLICATE_PLANNED_MEAL = 7;

    void onSuccess();
    void onFailure(int errorID);
}
