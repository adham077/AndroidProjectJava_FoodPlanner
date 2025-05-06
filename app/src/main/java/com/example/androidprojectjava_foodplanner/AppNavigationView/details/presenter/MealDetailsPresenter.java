package com.example.androidprojectjava_foodplanner.AppNavigationView.details.presenter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.example.androidprojectjava_foodplanner.AppNavigationView.details.view.MealDetailsContract;
import com.example.androidprojectjava_foodplanner.local.database.FavouriteMealCB;
import com.example.androidprojectjava_foodplanner.local.database.PlannedMealCB;
import com.example.androidprojectjava_foodplanner.main.view.LoadActivityComm;
import com.example.androidprojectjava_foodplanner.model.pojo.FavouriteMeal;
import com.example.androidprojectjava_foodplanner.model.pojo.Ingredient;
import com.example.androidprojectjava_foodplanner.model.pojo.Meal;
import com.example.androidprojectjava_foodplanner.model.pojo.PlannedMeal;
import com.example.androidprojectjava_foodplanner.model.repository.MealRepository;
import com.example.androidprojectjava_foodplanner.model.repository.OperationCB;
import com.example.androidprojectjava_foodplanner.model.repository.UserRepository;
import com.example.androidprojectjava_foodplanner.remote.meal.MealNetworkCB;
import com.example.androidprojectjava_foodplanner.remote.meal.ingredients.IngredientsNetworkCB;
import com.example.androidprojectjava_foodplanner.remote.meal.ingredients.IngredientsRemoteDataSource;
import com.google.firebase.firestore.auth.User;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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

        return instance = new MealDetailsPresenter(mealRepository, view, userRepository,loadActivity,ingredientsRemoteDataSource);
    }

    public void init(){
        if(!userRepository.isLoggedIn()){
            view.hideScheduleBtn();
            view.hideFavBtn();
        }
    }


    public void getMealDetails(int mealID, String senderID){

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
        }
        else if(senderID.equals("FavouriteFragment")){
            mealRepository.getFavouriteMealByMealIDLocal(mealID, new FavouriteMealCB() {
                @Override
                public void found(FavouriteMeal meal) {
                    if(meal == null)return;
                    Meal _meal = (Meal)meal;
                    List<String> formattedIngredients = new ArrayList<>();

                    for(int i=0;i<_meal.getIngredients().size();i++){
                        formattedIngredients.add((_meal.getIngredients().get(i)));
                    }

                    List<Meal>_meals = new ArrayList<>();
                    _meals.add(_meal);

                    AtomicInteger counter = new AtomicInteger(0);

                    class GetImages implements Runnable{
                        private List<Bitmap> image;
                        private List<Bitmap> ingredientImages;
                        private AtomicInteger counter;

                        GetImages(){
                            this.image = null;
                            this.ingredientImages = null;
                            counter = new AtomicInteger(0);
                        }

                        public void mealImageDone(List<Bitmap> image){
                            this.image = image;
                            this.run();
                        }

                        public void ingredientImageDone(List<Bitmap> ingredientImages){
                            this.ingredientImages = ingredientImages;
                            this.run();
                        }

                        @Override
                        public void run() {
                            if(counter.incrementAndGet() == 2){
                                view.showMealDetailsFromFav(image,ingredientImages,_meal);
                            }
                        }
                    };

                    GetImages getImages = new GetImages();

                    userRepository.getMealSavedImages(_meals, view.provideContext(), new UserRepository.MealImagesCB() {
                        @Override
                        public void onSuccess(List<Bitmap> imagesBitmap) {
                            getImages.mealImageDone(imagesBitmap);
                        }

                        @Override
                        public void onFailure(String message) {

                        }
                    });

                    ingredientsRemoteDataSource.getIngredientImagesFromLocal(formattedIngredients, new IngredientsNetworkCB() {
                        @Override
                        public void onSuccess(List<Bitmap> imagesBitmap) {
                            getImages.ingredientImageDone(imagesBitmap);
                        }

                        @Override
                        public void onSuccess(Bitmap imageBitmap) {

                        }

                        @Override
                        public void onFailure(String message) {

                        }
                    });

                }
            });
        }
        else{

        }
    }
    public void onFavClicked(int mealID){
        if(mealID == 0)return;
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
                                    Thread myThread = new Thread(new UserRepository.SaveMealImageToLocal(meal,view.provideContext()));
                                    myThread.start();
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

    public void onScheduleClicked(int mealID,int day,int month,int year){
        final PlannedMeal[] plannedMeal = {null};
        mealRepository.getMealByIdRemote(mealID, new MealNetworkCB() {
            @Override
            public void onSuccess(Meal meal) {
                plannedMeal[0] = new PlannedMeal(meal,day,month,year);
                savePlannedMeal(plannedMeal[0]);
            }

            @Override
            public void onFailure(String msg) {
                view.showSavingPlannedFailure();
            }
        });
    }

    private void savePlannedMeal(PlannedMeal plannedMeal){
        userRepository.addPlannedMeal(plannedMeal, new OperationCB() {
            @Override
            public void onSuccess() {
                view.showSavingPlannedSuccess();
            }

            @Override
            public void onFailure(int errorID) {
                view.showSavingPlannedFailure();
            }
        });
    }

    public void getMealDetailsByDate(String date){
        mealRepository.getPlannedMealByDateLocal(date, new PlannedMealCB() {
            @Override
            public void found(PlannedMeal plannedMeal) {
                if(plannedMeal == null)return;
                Meal _meal = plannedMeal.getMeal();
                List<String> formattedIngredients = new ArrayList<>();
                for(int i=0;i<_meal.getIngredients().size();i++){
                    formattedIngredients.add((_meal.getIngredients().get(i)));
                }
                List<Meal>_meals = new ArrayList<>();
                _meals.add(_meal);

                class GetImages implements Runnable{
                    private List<Bitmap> image;
                    private List<Bitmap> ingredientImages;
                    private AtomicInteger counter;

                    public GetImages(){
                        counter = new AtomicInteger(0);
                        image = new ArrayList<>();
                        ingredientImages = new ArrayList<>();
                    }

                    public void mealImageDone(List<Bitmap> image){
                        this.image = image;
                        this.run();
                    }

                    public void ingredientImageDone(List<Bitmap> ingredientImages){
                        this.ingredientImages = ingredientImages;
                        this.run();
                    }

                    @Override
                    public void run() {
                        if(counter.incrementAndGet() == 2){
                            view.showMealDetailsFromFav(image,ingredientImages,_meal);
                        }
                    }
                }

                GetImages getImages = new GetImages();


                ingredientsRemoteDataSource.getIngredientImagesFromLocal(formattedIngredients, new IngredientsNetworkCB() {
                    @Override
                    public void onSuccess(List<Bitmap> imagesBitmap) {
                        getImages.ingredientImageDone(imagesBitmap);
                    }

                    @Override
                    public void onSuccess(Bitmap imageBitmap) {

                    }

                    @Override
                    public void onFailure(String message) {

                    }
                });

                userRepository.getMealSavedImages(_meals, view.provideContext(), new UserRepository.MealImagesCB() {
                    @Override
                    public void onSuccess(List<Bitmap> imagesBitmap) {
                        getImages.mealImageDone(imagesBitmap);
                    }

                    @Override
                    public void onFailure(String message) {

                    }
                });
            }
        });
    }



}
