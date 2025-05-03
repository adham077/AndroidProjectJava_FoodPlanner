package com.example.androidprojectjava_foodplanner.main.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidprojectjava_foodplanner.AppNavigationView.navigationActivity.view.NavigationActivity;
import com.example.androidprojectjava_foodplanner.R;
import com.example.androidprojectjava_foodplanner.account.login.view.LoginActivity;
import com.example.androidprojectjava_foodplanner.local.database.MealLocalDataSource;
import com.example.androidprojectjava_foodplanner.main.presenter.MainPresenter;
import com.example.androidprojectjava_foodplanner.model.repository.UserRepository;
import com.example.androidprojectjava_foodplanner.remote.meal.MealRemoteDataSource;
import com.example.androidprojectjava_foodplanner.remote.user.firebase.firebaseAuth.UserAuthentication;
import com.example.androidprojectjava_foodplanner.remote.user.firebase.firebaseDB.UserRemoteDataSource;


public class MainActivity extends AppCompatActivity implements MainActivityContract{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        UserRepository userRepository = UserRepository.getInstance(
                MealLocalDataSource.getInstance(this),
                MealRemoteDataSource.getInstance(this),
                UserAuthentication.getInstance(),
                UserRemoteDataSource.getInstance(this.getApplicationContext())
        );

        MainPresenter presenter = MainPresenter.getInstance(userRepository,this);
        presenter.init();
    }

    @Override
    public void init(UserState state) {
        if(state == UserState.LOGGED_IN){
            Intent intent = new Intent(MainActivity.this, NavigationActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}