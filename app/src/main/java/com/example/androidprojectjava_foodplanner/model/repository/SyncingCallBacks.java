package com.example.androidprojectjava_foodplanner.model.repository;

public interface SyncingCallBacks {
    int NOT_LOGGED_IN = 0;
    int SUCCESS = 0;
    int NETWORK_ERROR = 0;

    public void taskCompleted(int status);
}
