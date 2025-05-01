package com.example.androidprojectjava_foodplanner.model.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.androidprojectjava_foodplanner.local.database.FavouriteMealCB;
import com.example.androidprojectjava_foodplanner.local.database.MealLocalDataSource;
import com.example.androidprojectjava_foodplanner.local.database.OperationState;
import com.example.androidprojectjava_foodplanner.local.database.PlannedMealCB;
import com.example.androidprojectjava_foodplanner.model.pojo.FavouriteMeal;
import com.example.androidprojectjava_foodplanner.model.pojo.PlannedMeal;
import com.example.androidprojectjava_foodplanner.remote.meal.CategoriesNetworkCB;
import com.example.androidprojectjava_foodplanner.remote.meal.CountriesNetworkCB;
import com.example.androidprojectjava_foodplanner.remote.meal.MealListNetworkCB;
import com.example.androidprojectjava_foodplanner.remote.meal.MealNetworkCB;
import com.example.androidprojectjava_foodplanner.remote.meal.MealRemoteDataSource;

import java.util.List;

public class MealRepository {
    private MealRemoteDataSource remoteDataSource;
    private MealLocalDataSource localDataSource;
    private static MealRepository instance = null;

    private MealRepository(MealRemoteDataSource remoteDataSource, MealLocalDataSource localDataSource){
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;

    }

    public static MealRepository getInstance(MealRemoteDataSource remoteDataSource, MealLocalDataSource localDataSource){
        if(instance == null){
            if(remoteDataSource == null){
                Log.i("MealRepositoryCon","remoteDataSource is null");
            }
            else{
                Log.i("MealRepositoryCon","remoteDataSource is not null");
            }
            instance = new MealRepository(remoteDataSource,localDataSource);
        }
        return instance;
    }

    public void getMealByIdRemote(int id, MealNetworkCB cb){
        remoteDataSource.getMealById(id,cb);
    }

    public void getRandomMealRemote(MealNetworkCB cb){
        remoteDataSource.getRandomMeal(cb);
    }

    public void getMealsByCountryRemote(String country, MealListNetworkCB cb){
        remoteDataSource.getMealsByCountry(country,cb);
    }

    public void getMealsByCategoryRemote(String category, MealListNetworkCB cb){
        remoteDataSource.getMealsByCategory(category,cb);
    }

    public void getCountriesRemote(CountriesNetworkCB cb){
        remoteDataSource.getCountries(cb);
    }

    public void getCategoriesRemote(CategoriesNetworkCB cb){
        remoteDataSource.getCategories(cb);
    }

    public void getMealsByNameRemote(String name, MealListNetworkCB cb){
        remoteDataSource.getMealsByName(name,cb);
    }

    public void getMealsByIngredientRemote(String ingredient, MealListNetworkCB cb){
        remoteDataSource.getMealsByIngredient(ingredient,cb);
    }

    public void getStoredPlannedMeals(PlannedMealsDBCallBack callBack){
        localDataSource.getAllPlannedMeals(callBack);
    }

    public void getStoredFavouriteMeals(FavouriteMealsDBCallBack callBack){
        localDataSource.getAllFavouriteMeals(callBack);
    }

    public void insertPlannedMealLocal(PlannedMeal meal){
        localDataSource.insertPlannedMeal(meal,null);
    }

    public void deletePlannedMealLocal(PlannedMeal meal){
        localDataSource.deletePlannedMeal(meal,null);
    }

    public void insertFavouriteMealLocal(FavouriteMeal meal){
        localDataSource.insertFavouriteMeal(meal,null);
    }

    public void deleteFavouriteMealLocal(FavouriteMeal meal){
        localDataSource.deleteFavouriteMeal(meal,null);
    }

    public void deleteAllPlannedMealsLocal(@Nullable OperationState operationState){
        localDataSource.deleteAllPlannedMeals(operationState);
    }

    public void deleteAllFavouriteMealsLocal(@Nullable OperationState operationState){
        localDataSource.deleteAllFavouriteMeals(operationState);
    }

    public void getFavouriteMealByMealIDLocal(int id, FavouriteMealCB favouriteMealCB){
        localDataSource.getFavouriteMealByMealID(id,favouriteMealCB);
    }

    public void getPlannedMealByMealIDLocal(int id, PlannedMealCB plannedMealCB){
        localDataSource.getPlannedMealByMealID(id,plannedMealCB);
    }

    public void getPlannedMealByDateLocal(String date, PlannedMealCB plannedMealCB){
        localDataSource.getPlannedMealByDate(date,plannedMealCB);
    }

    public void getPlannedMealByMealNameLocal(String name, PlannedMealCB plannedMealCB){
        localDataSource.getPlannedMealByMealName(name,plannedMealCB);
    }

    public void getPlannedMealByDateLocal(int day,int month,int year, PlannedMealCB plannedMealCB){
        localDataSource.getPlannedMealByDate(day,month,year,plannedMealCB);
    }

    public void deletePlannedMealByDateLocal(int day,int month,int year,@Nullable OperationState operationState){
        localDataSource.deletePlannedMealByDate(day,month,year,operationState);
    }

    public void deleteAllLocalMeals(OperationState operationState){
        localDataSource.deleteAllLocalMeals(operationState);
    }

    public void insertPlannedMealsLocal(List<PlannedMeal> meals, OperationState operationState){
        localDataSource.insertPlannedMeals(meals,operationState);
    }

    public void insertFavouriteMealsLocal(List<FavouriteMeal> meals,OperationState operationState){
        localDataSource.insertFavouriteMeals(meals,operationState);
    }
}
