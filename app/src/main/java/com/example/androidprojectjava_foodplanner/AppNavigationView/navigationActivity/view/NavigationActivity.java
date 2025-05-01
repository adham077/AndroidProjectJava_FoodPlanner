package com.example.androidprojectjava_foodplanner.AppNavigationView.navigationActivity.view;

import android.os.Bundle;
import android.view.Menu;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.androidprojectjava_foodplanner.AppNavigationView.home.view.HomeFragment;
import com.example.androidprojectjava_foodplanner.AppNavigationView.loading.view.LoadingFragment;
import com.example.androidprojectjava_foodplanner.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NavigationActivity extends AppCompatActivity implements FragComm {

    private FragmentManager fgMan;

    private Fragment homeFragment;

    private Fragment loadingFragment;

    private BottomNavigationView bottomNavigationView;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_navigation);

        fgMan = getSupportFragmentManager();

        homeFragment = new HomeFragment();
        loadingFragment = new LoadingFragment();

        showHome();
    }

    @Override
    public void showLoading() {
        fgMan.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.NavViewFrameLayout, loadingFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void showHome() {
        fgMan.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.NavViewFrameLayout, homeFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void popBack() {
        fgMan.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        fgMan.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.NavViewFrameLayout, homeFragment)
                .commit();
    }
}