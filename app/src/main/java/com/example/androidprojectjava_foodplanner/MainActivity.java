package com.example.androidprojectjava_foodplanner;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.example.androidprojectjava_foodplanner.local.database.MealLocalDataSource;
import com.example.androidprojectjava_foodplanner.model.pojo.Category;
import com.example.androidprojectjava_foodplanner.model.pojo.Country;
import com.example.androidprojectjava_foodplanner.model.pojo.FavouriteMeal;
import com.example.androidprojectjava_foodplanner.model.pojo.Meal;
import com.example.androidprojectjava_foodplanner.model.pojo.PlannedMeal;
import com.example.androidprojectjava_foodplanner.model.pojo.PlannerUser;
import com.example.androidprojectjava_foodplanner.remote.meal.CategoriesNetworkCB;
import com.example.androidprojectjava_foodplanner.remote.meal.CountriesNetworkCB;
import com.example.androidprojectjava_foodplanner.remote.meal.MealListNetworkCB;
import com.example.androidprojectjava_foodplanner.remote.meal.MealNetworkCB;
import com.example.androidprojectjava_foodplanner.remote.meal.MealRemoteDataSource;
import com.example.androidprojectjava_foodplanner.remote.user.firebase.firebaseAuth.OnSignupCallBack;
import com.example.androidprojectjava_foodplanner.remote.user.firebase.firebaseAuth.UserAuthentication;
import com.example.androidprojectjava_foodplanner.remote.user.firebase.firebaseDB.UserRemoteDataSource;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    PlannerUser myUser;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        /*myUser = new PlannerUser();
        UserAuthentication.getInstance().signUp("adham.walaa@gmail.com", "123", "Adham Walaa", new OnSignupCallBack() {
            @Override
            public void onSuccess(FirebaseUser user) {
                firebaseUser = UserAuthentication.getInstance().getCurrentUser();
                fireStoreTest();
            }

            @Override
            public void onFailure(int errorID) {

            }
        });*/

        //Intent intent  = new Intent(MainActivity.this, LoginActivity.class);
        //startActivity(intent);
        //dataBaseTest();
        //networkTest();
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

    public void dataBaseTest(){
        MealRemoteDataSource mealRemoteDataSource = MealRemoteDataSource.getInstance(this.getApplicationContext());
        MealLocalDataSource localDataSource = MealLocalDataSource.getInstance(this.getApplicationContext());
        mealRemoteDataSource.getMealById(52772, new MealNetworkCB() {
            @Override
            public void onSuccess(Meal meal) {
                localDataSource.insertPlannedMeal(new PlannedMeal(meal,1,1,2025),null);
                localDataSource.insertFavouriteMeal(new FavouriteMeal(meal),null);
            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }

    public void fireStoreTest(){
        MealLocalDataSource mealLocalDataSource = MealLocalDataSource.getInstance(this.getApplicationContext());
        UserRemoteDataSource userRemoteDataSource = UserRemoteDataSource.getInstance(this.getApplicationContext());
        myUser.setId(firebaseUser.getUid());
        myUser.setName(firebaseUser.getDisplayName());
        myUser.setEmail(firebaseUser.getEmail());
        mealLocalDataSource.getAllFavouriteMeals().observe(this, new Observer<List<FavouriteMeal>>() {
            @Override
            public void onChanged(List<FavouriteMeal> favouriteMeals) {
                myUser.setFavouriteMeals(favouriteMeals);
                userRemoteDataSource.addOrChangeUserDataById(myUser,null);
            }
        });

    }

}