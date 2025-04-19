package com.example.androidprojectjava_foodplanner;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidprojectjava_foodplanner.model.Category;
import com.example.androidprojectjava_foodplanner.model.Country;
import com.example.androidprojectjava_foodplanner.model.Meal;
import com.example.androidprojectjava_foodplanner.remote.meal.CategoriesNetworkCB;
import com.example.androidprojectjava_foodplanner.remote.meal.CountriesNetworkCB;
import com.example.androidprojectjava_foodplanner.remote.meal.MealListNetworkCB;
import com.example.androidprojectjava_foodplanner.remote.meal.MealNetworkCB;
import com.example.androidprojectjava_foodplanner.remote.meal.MealRemoteDataSource;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        networkTest();
    }

    public void networkTest(){
        MealRemoteDataSource remoteDataSource = MealRemoteDataSource.getInstance(this.getApplicationContext());
        remoteDataSource.getMealById(52772, new MealNetworkCB() {
            @Override
            public void onSuccess(Meal meal) {
                Log.i("MealById",meal.getName());
            }

            @Override
            public void onFailure(String msg) {

            }
        });

        remoteDataSource.getRandomMeal(new MealNetworkCB() {
            @Override
            public void onSuccess(Meal meal) {
                Log.i("RandomMeal",meal.getName());
                List<String> ingredients = meal.getIngredients();
                List<String> measurements = meal.getMeasurements();
                for(String str: ingredients){
                    Log.i("RandomMealIngredients",str);

                }
                Log.i("RandomMealIngredientsSize",Integer.toString(ingredients.size()));
                for(String str: measurements){
                    Log.i("RandomMealMeasurements",str);
                }
                Log.i("RandomMealMeasurmentsSize",Integer.toString(measurements.size()));
                Log.i("RandomMealVideoUrl",meal.getVideoUrl());
                Log.i("RandomMealImageUrl",meal.getImageUrl());
                Log.i("RandomMealIds",Integer.toString(meal.getId()));
            }

            @Override
            public void onFailure(String msg) {

            }
        });

        remoteDataSource.getMealsByCountry("Egyptian", new MealListNetworkCB() {
            @Override
            public void onSuccess(List<Meal> meals) {
                for(Meal meal: meals) {
                    Log.i("MealsByCountry",meal.getName());
                }
            }

            @Override
            public void onFailure(String msg) {

            }
        });

        remoteDataSource.getMealsByCategory("chicken", new MealListNetworkCB() {
            @Override
            public void onSuccess(List<Meal> meals) {
                for(Meal meal: meals) {
                    Log.i("MealsByCategory",meal.getName());
                }
            }

            @Override
            public void onFailure(String msg) {

            }
        });

        remoteDataSource.getMealsByName("Casserole", new MealListNetworkCB() {
            @Override
            public void onSuccess(List<Meal> meals) {
                for(Meal meal: meals) {
                    Log.i("MealsByCategory",meal.getName());
                }
            }

            @Override
            public void onFailure(String msg) {

            }
        });

        remoteDataSource.getMealsByIngredient("Banana", new MealListNetworkCB() {
            @Override
            public void onSuccess(List<Meal> meals) {
                for(Meal meal: meals) {
                    Log.i("MealsByIngredient",meal.getName());
                }
            }

            @Override
            public void onFailure(String msg) {

            }
        });

       remoteDataSource.getCategories(new CategoriesNetworkCB() {
            @Override
            public void onSuccess(List<Category> categories) {
                for(Category category: categories){
                    Log.i("categories",category.getName());
                }
            }

            @Override
            public void onFailure(String msg) {

            }
        });

        remoteDataSource.getCountries(new CountriesNetworkCB() {
            @Override
            public void onSuccess(List<Country> countries) {
                for(Country country: countries){
                    Log.i("Countries",country.getName());
                }
            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }

}