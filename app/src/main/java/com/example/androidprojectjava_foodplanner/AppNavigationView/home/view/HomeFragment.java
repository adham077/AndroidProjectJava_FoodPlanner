package com.example.androidprojectjava_foodplanner.AppNavigationView.home.view;


import static androidx.core.content.ContextCompat.registerReceiver;

import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.androidprojectjava_foodplanner.AppNavigationView.home.presenter.HomePresenter;
import com.example.androidprojectjava_foodplanner.AppNavigationView.navigationActivity.view.FragComm;
import com.example.androidprojectjava_foodplanner.R;
import com.example.androidprojectjava_foodplanner.local.database.MealLocalDataSource;
import com.example.androidprojectjava_foodplanner.model.pojo.Meal;
import com.example.androidprojectjava_foodplanner.model.repository.MealRepository;
import com.example.androidprojectjava_foodplanner.network.NetworkChange;
import com.example.androidprojectjava_foodplanner.remote.meal.MealNetworkCB;
import com.example.androidprojectjava_foodplanner.remote.meal.MealRemoteDataSource;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;



public class HomeFragment extends Fragment implements HomeContract{
    private RecyclerView recyclerView;
    private TextView randomMealTitle;
    private ImageView randomMealImage;
    private LinearLayout onlineView;
    private LinearLayout offlineView;
    private View myView;
    private MealRemoteDataSource mealRemoteDataSource;
    private MealLocalDataSource mealLocalDataSource;

    private  Meal randMeal;

    private int randMealId;
    private String randMealImageUrl;
    private String randMealTitle;
    private HomePresenter presenter;

    private boolean newDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        SharedPreferences sharedPref = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        if(savedInstanceState != null){
            randMealTitle = savedInstanceState.getString("mealTitle");
            randMealImageUrl = savedInstanceState.getString("mealImageUrl");
            randMealId = savedInstanceState.getInt("mealId");
        }
        else{
            randMealTitle = sharedPref.getString("mealTitle",null);
            randMealImageUrl = sharedPref.getString("mealImageUrl",null);
            randMealId = sharedPref.getInt("mealId",0);
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = currentDate.format(formatter);
            if(sharedPref.getString("date",null) == null){
                sharedPref.edit().putString("date",formattedDate).apply();
                newDate = true;
            }
            else if(!sharedPref.getString("date",null).equals(formattedDate)){
                sharedPref.edit().putString("date",formattedDate).apply();
                newDate = true;
            }
            else{
                newDate = false;
            }

        }

        recyclerView = view.findViewById(R.id.randomMealsRecyclerView);
        myView = view;
        onlineView = view.findViewById(R.id.online_content);
        offlineView = view.findViewById(R.id.offline_state);
        randomMealTitle = view.findViewById(R.id.mealTitleRandom);
        randomMealImage = view.findViewById(R.id.mealImageRandom);

        mealLocalDataSource = MealLocalDataSource.getInstance(this.getContext());
        mealRemoteDataSource = MealRemoteDataSource.getInstance(this.getContext());

        MealRepository mealRepository = MealRepository.getInstance(mealRemoteDataSource,mealLocalDataSource);
        presenter = HomePresenter.getInstance(mealRepository,this);
        FragComm fragComm = (FragComm)getActivity();
        presenter.setNavComm(fragComm);

        presenter.init();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        NetworkChange networkChange = presenter.getNetworkChange();
        registerReceiver(this.getContext(),networkChange, filter, ContextCompat.RECEIVER_EXPORTED);
    }



    @Override
    public void showOfflineView() {
        onlineView.setVisibility(View.GONE);
        offlineView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showOnlineView(@NonNull Meal randomMeal,@NonNull List<Meal> meals) {

        offlineView.setVisibility(View.GONE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                this.getContext(),
                LinearLayoutManager.HORIZONTAL,
                false
        );
        recyclerView.setLayoutManager(linearLayoutManager);
        int itemSpacing = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                16,
                getResources().getDisplayMetrics()
        );
        recyclerView.addItemDecoration(new ItemSpacingDecoration(itemSpacing));
        recyclerView.setAdapter(new HomeAdapter(meals));
        if (recyclerView.getOnFlingListener() == null) {
            SnapHelper snapHelper = new PagerSnapHelper();
            snapHelper.attachToRecyclerView(recyclerView);
        }

        if(newDate || randMealTitle == null) {
            randomMealTitle.setText(randomMeal.getName());
            Glide.with(myView.getContext()).
                    load(randomMeal.getImageUrl())
                    .into(randomMealImage);
            onlineView.setVisibility(View.VISIBLE);
            randMealImageUrl = randomMeal.getImageUrl();
            randMealTitle = randomMeal.getName();
            randMealId = randomMeal.getId();
            SharedPreferences sharedPref = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            sharedPref.edit().putString("mealTitle",randMealTitle).apply();
            sharedPref.edit().putString("mealImageUrl",randMealImageUrl).apply();
            sharedPref.edit().putInt("mealId",randMealId).apply();

        }
        else{
            randomMealTitle.setText(randMealTitle);
            Glide.with(myView.getContext()).
                    load(randMealImageUrl)
                    .into(randomMealImage);
            onlineView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("mealTitle",randMealTitle);
        outState.putString("mealImageUrl",randMealImageUrl);
        outState.putInt("mealId",randMealId);
    }

    @Override
    public void failedtoFetch() {
        Toast.makeText(this.getContext(),"failed to fetch", Toast.LENGTH_SHORT).show();
    }

    @Override
    public Object getAppContext() {
        return this.getContext();
    }


}

