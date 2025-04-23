package com.example.androidprojectjava_foodplanner.remote.user.firebase.firebaseDB;

import com.example.androidprojectjava_foodplanner.model.pojo.PlannerUser;

public interface GetUserCB{
    void onSuccess(PlannerUser plannerUser);
    void onFailure();
}
