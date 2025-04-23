package com.example.androidprojectjava_foodplanner.remote.user.firebase.firebaseAuth;

import com.google.firebase.auth.FirebaseUser;

public interface OnLoginCallBack {
    int SUCCESSFUL_LOGIN = 0;
    int INVALID_EMAIL_OR_PASSWORD = 1;
    int INVALID_USER = 2;
    int NETWORK_ERROR = 3;
    int AuthentiCAtION_FAILED = 4;

    public void onSuccess(FirebaseUser user);
    public void onFailure(int errorID);
}
