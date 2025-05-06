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


import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class HomePresenter {
    private  FragComm fragComm;
    private static   HomePresenter instance = null;
    private  NetworkChange networkChange;
    private  MyListener listener;
    private  MealRepository mealRepository;
    private  HomeContract view;

    private HomePresenter(MealRepository mealRepository, HomeContract view,NetworkChange networkChange){
        this.mealRepository = mealRepository;
        this.view = view;
        this.networkChange = networkChange;
        listener = new MyListener();
        networkChange.setListener(listener);
    }

    public static HomePresenter getInstance(MealRepository mealRepository, HomeContract view,
                                             NetworkChange networkChange) {
         return instance = new HomePresenter(mealRepository,view,networkChange);
    }

    private void fetchMeals(){
        if (view == null || !view.isFragmentActive()) return;

        Log.i("Aragorn_21","Fetching Meals");
        //fragComm.showLoading();
        AtomicBoolean fetchedRandom = new AtomicBoolean(false);
        AtomicBoolean fetchedList = new AtomicBoolean(false);
        final List[] _meals = new List[1];
        final Meal[] randomMeal = new Meal[1];

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(fetchedRandom.get() && fetchedList.get()){
                    Log.i("Aragorn_21","Fetching Successfuls");
                    //fragComm.showHome();
                    view.showOnlineView(randomMeal[0],_meals[0]);
                }
            }
        };

        mealRepository.getRandomMealRemote(new MealNetworkCB() {
            @Override
            public void onSuccess(Meal meal) {
                fetchedRandom.set(true);
                randomMeal[0] = meal;
                runnable.run();
            }

            @Override
            public void onFailure(String msg) {
                view.failedtoFetch();
            }
        });

        mealRepository.getMealsByNameRemote("chicken", new MealListNetworkCB() {
            @Override
            public void onSuccess(List<Meal> meals) {
                fetchedList.set(true);
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

    class MyListener implements NetworkListener{
        private boolean isFetching = false;
        @Override
        public void onChange(boolean isConnected) {
            if (isConnected && !isFetching) {
                isFetching = true;
                fetchMeals();
                isFetching = false;
            } else {
                view.showOfflineView();
            }
        }
    }

    public void getMealDetails(int mealId){
        if(networkChange.isConnected(view.getAppContext())){
            view.showMealDetails(mealId);
        }
        else{
            view.failedtoFetch();
        }
    }

    public void setNavComm(FragComm fragComm){
        this.fragComm = fragComm;
    }

    public NetworkChange getNetworkChange(){
        return networkChange;
    }

}
