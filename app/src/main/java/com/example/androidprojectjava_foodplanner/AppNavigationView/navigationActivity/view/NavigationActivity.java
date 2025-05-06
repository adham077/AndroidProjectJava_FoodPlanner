package com.example.androidprojectjava_foodplanner.AppNavigationView.navigationActivity.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import com.example.androidprojectjava_foodplanner.AppNavigationView.calendar.view.CalendarFragment;
import com.example.androidprojectjava_foodplanner.AppNavigationView.details.view.MealDetailsActivity;
import com.example.androidprojectjava_foodplanner.AppNavigationView.favourite.view.FavouriteFragment;
import com.example.androidprojectjava_foodplanner.AppNavigationView.guestProfile.view.GuestProfileFragment;
import com.example.androidprojectjava_foodplanner.AppNavigationView.home.view.HomeFragment;
import com.example.androidprojectjava_foodplanner.AppNavigationView.loading.view.LoadingFragment;
import com.example.androidprojectjava_foodplanner.AppNavigationView.navigationActivity.presenter.NavigationPresenter;
import com.example.androidprojectjava_foodplanner.AppNavigationView.profile.view.ProfileFragment;
import com.example.androidprojectjava_foodplanner.AppNavigationView.search.view.SearchFragment;
import com.example.androidprojectjava_foodplanner.R;
import com.example.androidprojectjava_foodplanner.local.database.MealLocalDataSource;
import com.example.androidprojectjava_foodplanner.model.repository.UserRepository;
import com.example.androidprojectjava_foodplanner.remote.meal.MealRemoteDataSource;
import com.example.androidprojectjava_foodplanner.remote.user.firebase.firebaseAuth.UserAuthentication;
import com.example.androidprojectjava_foodplanner.remote.user.firebase.firebaseDB.UserRemoteDataSource;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.auth.User;

public class NavigationActivity extends AppCompatActivity implements FragComm {
    private FragmentManager fgMan;
    private Fragment homeFragment;
    private Fragment loadingFragment;
    private Fragment profileFragment;
    private Fragment searchFragment;
    private Fragment calendarFragment;
    private Fragment favouritesFragment;
    private Fragment guestProfileFragment;
    private BottomNavigationView bottomNavigationView;
    private  NavigationPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_navigation);

        UserRepository userRepository = UserRepository.getInstance(
                MealLocalDataSource.getInstance(this.getApplicationContext()),
                MealRemoteDataSource.getInstance(this.getApplicationContext()),
                UserAuthentication.getInstance(),
                UserRemoteDataSource.getInstance(this.getApplicationContext())
        );

        presenter = NavigationPresenter.getInstance(userRepository,this);

        fgMan = getSupportFragmentManager();

        homeFragment = new HomeFragment();
        loadingFragment = new LoadingFragment();
        searchFragment = new SearchFragment();
        profileFragment = new ProfileFragment();
        guestProfileFragment = new GuestProfileFragment();
        calendarFragment = new CalendarFragment();
        favouritesFragment = new FavouriteFragment();


        bottomNavigationView = findViewById(R.id.NavViewMenuBar);
        bottomNavigationView.setSelectedItemId(R.id.homeNavBar);
        showHome();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id == R.id.homeNavBar){
                    presenter.changeState(NavigationPresenter.NavigationState.HOME);
                }
                else if(id == R.id.searchNavBar){
                    presenter.changeState(NavigationPresenter.NavigationState.SEARCH);
                }
                else if(id == R.id.calendarNavBar){
                    presenter.changeState(NavigationPresenter.NavigationState.CALENDAR);
                }
                else if(id == R.id.favouriteNavBar){
                    presenter.changeState(NavigationPresenter.NavigationState.FAVOURITE);
                }
                else if(id == R.id.accountNavBar){
                    presenter.changeState(NavigationPresenter.NavigationState.ACCOUNT);
                    Log.i("ClickedOnonon","accont");
                }
                else{
                    presenter.changeState(NavigationPresenter.NavigationState.HOME);
                }
                return true;
            }
        });
    }



    @Override
    public void showLoading() {
        fgMan.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.NavViewFrameLayout, loadingFragment)
                .commitAllowingStateLoss();
    }

    @Override
    public void showHome() {
        fgMan.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.NavViewFrameLayout, homeFragment)
                .commitAllowingStateLoss();
    }

    @Override
    public void showProfile(){
        fgMan.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.NavViewFrameLayout, profileFragment)
                .commitAllowingStateLoss();
    }

    @Override
    public void showFavourite(){
        fgMan.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.NavViewFrameLayout, favouritesFragment)
                .commitAllowingStateLoss();
    }

    @Override
    public void showGuestProfile(){
        fgMan.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.NavViewFrameLayout, guestProfileFragment)
                .commitAllowingStateLoss();
    }

    @Override
    public void showCalendar(){
        fgMan.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.NavViewFrameLayout, calendarFragment)
                .commitAllowingStateLoss();
    }

    @Override
    public void showSearch(){
        fgMan.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.NavViewFrameLayout, searchFragment)
                .commitAllowingStateLoss();
    }

    @Override
    public void showDetailsActivity(int MealId,String sender) {
        Intent intent = new Intent(this, MealDetailsActivity.class);
        intent.putExtra("senderID","HomeFragment");
        intent.putExtra("mealId",MealId);
        startActivity(intent);
    }

    @Override
    public void popBack() {
        fgMan.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        fgMan.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.NavViewFrameLayout, homeFragment)
                .commitAllowingStateLoss();
    }
}