package com.example.androidprojectjava_foodplanner.AppNavigationView.profile.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.androidprojectjava_foodplanner.AppNavigationView.navigationActivity.view.FragComm;
import com.example.androidprojectjava_foodplanner.AppNavigationView.profile.presenter.ProfilePresenter;
import com.example.androidprojectjava_foodplanner.R;
import com.example.androidprojectjava_foodplanner.account.login.view.LoginActivity;
import com.example.androidprojectjava_foodplanner.local.database.MealLocalDataSource;
import com.example.androidprojectjava_foodplanner.main.view.MainActivity;
import com.example.androidprojectjava_foodplanner.model.repository.UserRepository;
import com.example.androidprojectjava_foodplanner.remote.meal.MealRemoteDataSource;
import com.example.androidprojectjava_foodplanner.remote.user.firebase.firebaseAuth.UserAuthentication;
import com.example.androidprojectjava_foodplanner.remote.user.firebase.firebaseDB.UserRemoteDataSource;

public class ProfileFragment extends Fragment implements ProfileContract{
    private TextView tvUserName;
    private ImageButton btnEditName;
    private Button btnLogout;
    private Button btnFavoriteMeals;
    private Button btnPlannedMeals;
    private ProfilePresenter presenter;
    private  FragComm fragComm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        tvUserName = view.findViewById(R.id.tvUserName);
        btnEditName = view.findViewById(R.id.btnEditName);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnFavoriteMeals = view.findViewById(R.id.btnFavoriteMeals);
        btnPlannedMeals = view.findViewById(R.id.btnPlannedMeals);
        UserRepository userRepository = UserRepository.getInstance(
                MealLocalDataSource.getInstance(requireContext()),
                MealRemoteDataSource.getInstance(this),
                UserAuthentication.getInstance(),
                UserRemoteDataSource.getInstance(this)
        );
        presenter = ProfilePresenter.getInstance(this,userRepository);
        presenter.init();

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutConfirm();
            }
        });

        btnFavoriteMeals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFavouriteMeals();
            }
        });

        btnPlannedMeals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPlannedMeals();
            }
        });

        fragComm = (FragComm) getActivity();
    }

    @Override
    public void showEditNameDial() {

    }

    @Override
    public void showLogoutConfirm() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (dialog, which) -> performLogout())
                .setNegativeButton("Cancel", null)
                .create()
                .show();

    }

    public void performLogout(){
        presenter.logout();
    }

    @Override
    public void openFavouriteMeals() {
        fragComm.showFavourite();
    }

    @Override
    public void openPlannedMeals() {
        fragComm.showCalendar();
    }

    @Override
    public void goToLoginHome() {
        Intent intent = new Intent(requireContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void init(String name){
        tvUserName.setText(name);
    }
}