package com.example.androidprojectjava_foodplanner.login.presenter;

import android.util.Log;

import com.example.androidprojectjava_foodplanner.login.view.LoginViewContract;
import com.example.androidprojectjava_foodplanner.remote.user.firebase.firebaseAuth.OnLoginCallBack;
import com.example.androidprojectjava_foodplanner.remote.user.firebase.firebaseAuth.UserAuthentication;
import com.google.firebase.auth.FirebaseUser;

public class LoginPresenter {
    private LoginViewContract view;
    private static LoginPresenter instance = null;
    private UserAuthentication user;
    private LoginPresenter(){
        user = UserAuthentication.getInstance();
    }

    public static LoginPresenter getInstance(){
        if(instance == null){
            instance = new LoginPresenter();
        }
        return instance;
    }

    public void LoginWithEmailAndPassword(String email,String password){

    }

    public void LoginWithGoogleAccount(String idToken){

        user.signInWithGoogleAccount(idToken, new OnLoginCallBack() {
            @Override
            public void onSuccess(FirebaseUser user) {
                Log.i("GoogleSignIn","Success");
            }

            @Override
            public void onFailure(int errorID) {
                Log.i("OnFailureForAdham","Failure " + errorID);
            }
        });
    }

}
