package com.example.androidprojectjava_foodplanner.login.view;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidprojectjava_foodplanner.R;
import com.example.androidprojectjava_foodplanner.remote.firebase.firebaseAuth.UserAuthentication;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {
    TextInputLayout textInputLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        UserAuthentication userAuthentication = UserAuthentication.getInstance();
        /*userAuthentication.signUp("adham.walaa@gmail.com", "Aragorn_21", new OnSignupCallBack() {
            @Override
            public void onSuccess(FirebaseUser user) {
                Log.i("success","sucess");
            }

            @Override
            public void onFailure(int errorID) {
                Log.i("Failure","Failure" + errorID);
            }
        });*/

        //userAuthentication.logOut();

        /*userAuthentication.login("adham.walaa@gmail.com", "Aragorn_21", new OnLoginCallBack() {
            @Override
            public void onSuccess(FirebaseUser user) {

            }

            @Override
            public void onFailure(int errorID) {
                Log.i("Failure","Failure" + errorID);
            }
        });*/
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle saveInstanceState){
        super.onSaveInstanceState(saveInstanceState);

    }

}