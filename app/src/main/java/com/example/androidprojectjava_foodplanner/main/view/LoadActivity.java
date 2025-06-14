package com.example.androidprojectjava_foodplanner.main.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.androidprojectjava_foodplanner.AppNavigationView.details.view.MealDetailsActivity;
import com.example.androidprojectjava_foodplanner.R;

public class LoadActivity extends AppCompatActivity implements LoadActivityComm {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_load);
    }

    @Override
    public void ret() {
        finish();
    }
}