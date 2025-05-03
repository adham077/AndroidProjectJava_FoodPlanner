package com.example.androidprojectjava_foodplanner.AppNavigationView.details.presenter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.example.androidprojectjava_foodplanner.AppNavigationView.details.view.MealDetailsContract;
import com.example.androidprojectjava_foodplanner.local.database.FavouriteMealCB;
import com.example.androidprojectjava_foodplanner.main.view.LoadActivityComm;
import com.example.androidprojectjava_foodplanner.model.pojo.FavouriteMeal;
import com.example.androidprojectjava_foodplanner.model.pojo.Ingredient;
import com.example.androidprojectjava_foodplanner.model.pojo.Meal;
import com.example.androidprojectjava_foodplanner.model.repository.MealRepository;
import com.example.androidprojectjava_foodplanner.model.repository.OperationCB;
import com.example.androidprojectjava_foodplanner.model.repository.UserRepository;
import com.example.androidprojectjava_foodplanner.remote.meal.MealNetworkCB;
import com.example.androidprojectjava_foodplanner.remote.meal.ingredients.IngredientsNetworkCB;
import com.example.androidprojectjava_foodplanner.remote.meal.ingredients.IngredientsRemoteDataSource;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MealDetailsPresenter {
    private MealRepository mealRepository;
    private MealDetailsContract view;
    private UserRepository userRepository;
    private LoadActivityComm loadActivityComm;
    private IngredientsRemoteDataSource ingredientsRemoteDataSource;
    private static MealDetailsPresenter instance = null;

    private MealDetailsPresenter(MealRepository mealRepository,
                                 MealDetailsContract view,
                                 UserRepository userRepository,
                                 LoadActivityComm loadActivityComm,
                                 IngredientsRemoteDataSource ingredientsRemoteDataSource) {
        this.mealRepository = mealRepository;
        this.view = view;
        this.userRepository = userRepository;
        this.loadActivityComm = loadActivityComm;
        this.ingredientsRemoteDataSource = ingredientsRemoteDataSource;
    }

    public static MealDetailsPresenter getInstance(MealRepository mealRepository,
                                                   MealDetailsContract view,
                                                   UserRepository userRepository,
                                                   LoadActivityComm loadActivity,
                                                   IngredientsRemoteDataSource ingredientsRemoteDataSource) {
        if(instance == null){
            instance = new MealDetailsPresenter(mealRepository, view, userRepository,loadActivity,ingredientsRemoteDataSource);
        }
        return instance;
    }



    public void getMealDetails(int mealID, String senderID){

        //view.showLoading();
        if(senderID.equals("HomeFragment")) {
            Log.i("MealDetailsPresenterMealId", "" + mealID);
            mealRepository.getMealByIdRemote(mealID, new MealNetworkCB() {
                @Override
                public void onSuccess(Meal meal) {
                    List<String> formattedIngredients = new ArrayList<>();
                    for(int i=0;i<meal.getIngredients().size();i++){
                        formattedIngredients.add((meal.getIngredients().get(i)));
                    }
                    ingredientsRemoteDataSource.getIngredientsImages(formattedIngredients, new IngredientsNetworkCB() {
                        @Override
                        public void onSuccess(List<Bitmap> imagesBitmap) {
                            List<Ingredient> ingredientList = new ArrayList<>();
                            for(int i=0;i<meal.getIngredients().size();i++){
                                ingredientList.add(new Ingredient(meal.getIngredients().get(i),meal.getMeasurements().get(i),imagesBitmap.get(i)));
                            }
                            //loadActivityComm.ret();

                            view.showMealDetails(meal, ingredientList);

                        }

                        @Override
                        public void onSuccess(Bitmap imageBitmap) {

                        }

                        @Override
                        public void onFailure(String message) {
                            Log.i("FailedFetching","failed" + message);
                            //loadActivityComm.ret();
                            view.failedToFetch();
                        }
                    });
                }
                @Override
                public void onFailure(String msg) {
                    //loadActivityComm.ret();
                    view.failedToFetch();
                }
            });

            mealRepository.getFavouriteMealByMealIDLocal(mealID, new FavouriteMealCB() {
                @Override
                public void found(FavouriteMeal meal) {
                    if(meal != null){
                        view.highlightFav();
                    }
                    else{
                        view.unhighlightFav();
                    }
                }
            });
        }
    }
    public void onFavClicked(int mealID){
        mealRepository.getFavouriteMealByMealIDLocal(mealID, new FavouriteMealCB() {
            @Override
            public void found(FavouriteMeal meal) {
                if(meal != null){
                    FavouriteMeal favouriteMeal = new FavouriteMeal(meal);
                    userRepository.removeFavouriteMeal(meal, new OperationCB() {
                        @Override
                        public void onSuccess() {
                            view.unhighlightFav();
                        }

                        @Override
                        public void onFailure(int errorID) {
                            /*Do nothing*/
                        }
                    });
                }
                else{
                    mealRepository.getMealByIdRemote(mealID, new MealNetworkCB() {
                        @Override
                        public void onSuccess(Meal meal) {
                            userRepository.addFavouriteMeal(new FavouriteMeal(meal), new OperationCB() {
                                @Override
                                public void onSuccess() {
                                    view.highlightFav();
                                    List<String>ingredientsNames = new ArrayList<>();
                                    for(int i=0;i<meal.getIngredients().size();i++){
                                        ingredientsNames.add(meal.getIngredients().get(i));
                                    }
                                    ingredientsRemoteDataSource.getIngredientsImages(
                                            ingredientsNames,
                                            new IngredientsRemoteDataSource.SaveIngredients(ingredientsNames)
                                    );
                                }

                                @Override
                                public void onFailure(int errorID) {

                                }
                            });
                        }

                        @Override
                        public void onFailure(String msg) {

                        }
                    });
                }
            }
        });
    }
}
