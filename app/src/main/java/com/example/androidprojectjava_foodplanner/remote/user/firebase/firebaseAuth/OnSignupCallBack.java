package com.example.androidprojectjava_foodplanner.remote.user.firebase.firebaseAuth;

import com.google.firebase.auth.FirebaseUser;

public interface OnSignupCallBack {
    int SUCCESSFUL_SIGNUP = 0;
    int INVALID_EMAIL_OR_PASSWORD = 1;
    int INVALID_EMAIL = 2;
    int NETWORK_ERROR = 3;
    int USER_EXISTS_ERROR = 4;
    int PASSWORD_LENGTH_ERROR = 5;
    int AuthentiCAtION_FAILED = 6;
    int PROFILE_UPDATE_ERROR = 7;

    public void onSuccess(FirebaseUser user);
    public void onFailure(int errorID);
}
