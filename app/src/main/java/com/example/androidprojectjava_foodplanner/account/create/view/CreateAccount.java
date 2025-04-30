package com.example.androidprojectjava_foodplanner.account.create.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.androidprojectjava_foodplanner.R;
import com.example.androidprojectjava_foodplanner.account.create.presenter.CreateAccountPresenter;
import com.example.androidprojectjava_foodplanner.local.database.MealLocalDataSource;
import com.example.androidprojectjava_foodplanner.model.repository.UserRepository;
import com.example.androidprojectjava_foodplanner.remote.user.firebase.firebaseAuth.UserAuthentication;
import com.example.androidprojectjava_foodplanner.remote.user.firebase.firebaseDB.UserRemoteDataSource;

public class CreateAccount extends AppCompatActivity implements CreateAccountContract{
    CreateAccountPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_account);
        createBtnHandler();
        UserRepository userRepository = UserRepository.getInstance(
                MealLocalDataSource.getInstance(this),
                UserAuthentication.getInstance(),
                UserRemoteDataSource.getInstance(this)
        );
        presenter = CreateAccountPresenter.getInstance(userRepository,this);
    }

    public void createBtnHandler(){
        Button createBtn = findViewById(R.id.CreateActivityCreateAccountButton);
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}