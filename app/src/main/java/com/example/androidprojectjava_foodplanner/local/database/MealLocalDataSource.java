package com.example.androidprojectjava_foodplanner.local.database;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.example.androidprojectjava_foodplanner.model.pojo.FavouriteMeal;
import com.example.androidprojectjava_foodplanner.model.pojo.PlannedMeal;

import java.util.List;

public class MealLocalDataSource {
    private FavouriteMealDao favouriteMealDao;
    private PlannedMealDao plannedMealDao;
    private static MealLocalDataSource instance = null;

    MealLocalDataSource(Object context){
        AppDataBase appDataBase = AppDataBase.getInstance(context);
        favouriteMealDao = appDataBase.getFavouriteMealDao();
        plannedMealDao = appDataBase.getPlannedMealDao();
    }

    public static MealLocalDataSource getInstance(Object context){
        if(instance == null){
            instance = new MealLocalDataSource(context);
        }
        return instance;
    }

    public void insertFavouriteMeal(FavouriteMeal favouriteMeal,@Nullable OperationState operationState){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (favouriteMealDao.getFavouriteMealByMealID(favouriteMeal.getId()) == null) {
                    favouriteMealDao.insertFavouriteMeal(favouriteMeal);
                    if(operationState !=null)operationState.onSuccess();
                    Log.i("LocalDataBaseFav","sucess");
                }
                else{

                    if(operationState != null)operationState.onFailure();
                    Log.i("LocalDataBaseFav","fail");
                }
            }
        }).start();
    }

    public void deleteFavouriteMeal(FavouriteMeal favouriteMeal,@Nullable OperationState operationState){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (favouriteMealDao.getFavouriteMealByMealID(favouriteMeal.getId()) != null) {
                    favouriteMealDao.deleteFavouriteMeal(favouriteMeal);
                    if(operationState != null)operationState.onSuccess();
                }
                else{
                    if(operationState != null)operationState.onFailure();
                }
            }
        }).start();
    }

    public LiveData<List<FavouriteMeal>> getAllFavouriteMeals(){
        return favouriteMealDao.getAllFavouriteMeals();
    }

    public void insertPlannedMeal(PlannedMeal plannedMeal,@Nullable OperationState operationState){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(plannedMealDao.getPlannedMealByDate(plannedMeal.getDate()) == null){
                    plannedMealDao.insertPlannedMeal(plannedMeal);
                    if(operationState != null)operationState.onSuccess();
                }
                else{
                    if(operationState != null)operationState.onFailure();
                }
            }
        }).start();
    }

    public void deletePlannedMeal(PlannedMeal plannedMeal,@Nullable OperationState operationState){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(plannedMealDao.getPlannedMealByDate(plannedMeal.getDate()) != null){
                    plannedMealDao.insertPlannedMeal(plannedMeal);
                    if(operationState != null)operationState.onSuccess();
                }
                else{
                    if(operationState != null)operationState.onFailure();
                }
            }
        }).start();
    }

    public LiveData<List<PlannedMeal>> getAllPlannedMeals(){
        return plannedMealDao.getAllPlannedMeals();
    }

    public void deleteAllPlannedMeals(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                plannedMealDao.deleteAllPlannedMeals();
            }
        }).start();

    }

    public void deleteAllFavouriteMeals(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                favouriteMealDao.deleteAllFavouriteMeals();
                Log.i("LocalDataBaseFav","sucessDeletion");
            }
        }).start();
    }

    public void getFavouriteMealByMealID(int id, FavouriteMealCB favouriteMealCB){
        new Thread(new Runnable() {
            @Override
            public void run() {
                FavouriteMeal favouriteMeal = favouriteMealDao.getFavouriteMealByMealID(id);
                favouriteMealCB.found(favouriteMeal);
            }
        }).start();
    }

    public void getPlannedMealByMealID(int id, PlannedMealCB plannedMealCB){
        new Thread(new Runnable() {
            @Override
            public void run() {
                PlannedMeal plannedMeal = plannedMealDao.getPlannedMealByMealID(id);
                plannedMealCB.found(plannedMeal);
                }
        }).start();
    }

    public void getPlannedMealByDate(String date, PlannedMealCB plannedMealCB){
        new Thread(new Runnable() {
            @Override
            public void run() {
                PlannedMeal plannedMeal = plannedMealDao.getPlannedMealByDate(date);
                plannedMealCB.found(plannedMeal);
                }
        }).start();
    }

    public void getPlannedMealByMealName(String name, PlannedMealCB plannedMealCB){
        new Thread(new Runnable() {
            @Override
            public void run() {
                PlannedMeal plannedMeal = plannedMealDao.getPlannedMealByMealName(name);
                plannedMealCB.found(plannedMeal);
                }
        }).start();
    }

    public void getPlannedMealByDate(int day,int month,int year, PlannedMealCB plannedMealCB){
        new Thread(new Runnable() {
            @Override
            public void run() {
                PlannedMeal plannedMeal = plannedMealDao.getPlannedMealByDate(day,month,year);
                plannedMealCB.found(plannedMeal);
                }
        }).start();
    }

    public void deletePlannedMealByDate(int day,int month,int year){
        new Thread(new Runnable() {
            @Override
            public void run() {
                plannedMealDao.deletePlannedMealByDate(day,month,year);
            }
        }).start();
    }
}
