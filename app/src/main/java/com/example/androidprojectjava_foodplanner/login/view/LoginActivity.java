package com.example.androidprojectjava_foodplanner.login.view;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.androidprojectjava_foodplanner.R;
import com.example.androidprojectjava_foodplanner.model.User;

public class LoginActivity extends AppCompatActivity {
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle saveInstanceState){
        super.onSaveInstanceState(saveInstanceState);

    }

}