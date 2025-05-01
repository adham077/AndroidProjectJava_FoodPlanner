package com.example.androidprojectjava_foodplanner.AppNavigationView.home.presenter;

import android.util.Log;

import com.example.androidprojectjava_foodplanner.AppNavigationView.home.view.HomeContract;
import com.example.androidprojectjava_foodplanner.AppNavigationView.navigationActivity.view.FragComm;
import com.example.androidprojectjava_foodplanner.model.pojo.Meal;
import com.example.androidprojectjava_foodplanner.model.repository.MealRepository;
import com.example.androidprojectjava_foodplanner.network.NetworkChange;
import com.example.androidprojectjava_foodplanner.network.NetworkListener;
import com.example.androidprojectjava_foodplanner.remote.meal.MealListNetworkCB;
import com.example.androidprojectjava_foodplanner.remote.meal.MealNetworkCB;
import com.example.androidprojectjava_foodplanner.remote.meal.MealRemoteDataSource;

import java.util.List;

public class HomePresenter {
    private static FragComm fragComm;
    private static HomePresenter instance = null;
    private static NetworkChange networkChange;
    private static MyListener listener;
    private static MealRepository mealRepository;
    private static HomeContract view;

    private HomePresenter(MealRepository mealRepository, HomeContract view){
        HomePresenter.mealRepository = mealRepository;
        HomePresenter.view = view;
    }

    public static HomePresenter getInstance(MealRepository mealRepository, HomeContract view) {
        if(instance == null){
            instance = new HomePresenter(mealRepository,view);
        }
        listener = new MyListener();
        networkChange = NetworkChange.getInstance();
        networkChange.setListener(listener);
        return instance;
    }

    private static void fetchMeals(){
        fragComm.showLoading();
        final boolean[] fetched = {false,false};
        final List[] _meals = new List[1];
        final Meal[] randomMeal = new Meal[1];

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(fetched[0] && fetched[1]){
                    fragComm.showHome();
                    view.showOnlineView(randomMeal[0],_meals[0]);
                }
            }
        };


        MealRemoteDataSource.getInstance(view.getAppContext()).getRandomMeal(new MealNetworkCB() {
            @Override
            public void onSuccess(Meal meal) {
                fetched[0] = true;
                randomMeal[0] = meal;
                runnable.run();
            }

            @Override
            public void onFailure(String msg) {
                view.failedtoFetch();
            }
        });

        MealRemoteDataSource.getInstance(view.getAppContext()).getMealsByName("chicken", new MealListNetworkCB() {
            @Override
            public void onSuccess(List<Meal> meals) {
                fetched[1] = true;
                _meals[0] = meals;
                runnable.run();
            }

            @Override
            public void onFailure(String msg) {
                view.failedtoFetch();
            }
        });
    }
    public void init(){
        if(networkChange.isConnected(view.getAppContext())){
            fetchMeals();
        }
        else{
            view.showOfflineView();
        }
    }

    static class MyListener implements NetworkListener{
        @Override
        public void onChange(boolean isConnected) {
            if(isConnected){
                fetchMeals();
            }
            else{
                view.showOfflineView();
            }
        }
    }

    public void setNavComm(FragComm fragComm){
        HomePresenter.fragComm = fragComm;
    }

    public NetworkChange getNetworkChange(){
        return networkChange;
    }

}
