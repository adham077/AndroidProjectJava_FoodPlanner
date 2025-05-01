package com.example.androidprojectjava_foodplanner.remote.meal;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MealRemoteDataSource {
    private static MealRemoteDataSource instance = null;
    private static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";

    private final MealNetworkService mealService;

    private MealRemoteDataSource(Object context){
        Context _context = (Context)context;
        int cacheSize = 10 * 1024 * 1024;
        Cache cache = new Cache( _context.getCacheDir(), cacheSize);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().cache(cache).build();

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = retrofitBuilder.build();
        mealService =retrofit.create(MealNetworkService.class);
    }

    public static MealRemoteDataSource getInstance(Object context){
        if(instance == null){
            instance = new MealRemoteDataSource(context);
        }
        return instance;
    }

    public void getMealById(int id,MealNetworkCB callback){
        Call<MealNetworkWrapper> call = mealService.getMealById(id);
        call.enqueue(new Callback<MealNetworkWrapper>() {
            @Override
            public void onResponse(@NonNull Call<MealNetworkWrapper> call, @NonNull Response<MealNetworkWrapper> response) {
                assert response.body() != null;
                callback.onSuccess(response.body().getMeals().get(0));
            }

            @Override
            public void onFailure(@NonNull Call<MealNetworkWrapper> call, @NonNull Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    public void getRandomMeal(MealNetworkCB callBack){

        Call<MealNetworkWrapper> call = mealService.getRandomMeal();
        call.enqueue(new Callback<MealNetworkWrapper>() {
            @Override
            public void onResponse(@NonNull Call<MealNetworkWrapper> call, @NonNull Response<MealNetworkWrapper> response) {
                assert response.body() != null;
                callBack.onSuccess(response.body().getMeals().get(0));
            }

            @Override
            public void onFailure(@NonNull Call<MealNetworkWrapper> call, @NonNull Throwable t) {
                callBack.onFailure(t.getMessage());
            }
        });
    }

    public void getMealsByName(String name,MealListNetworkCB callback){
        Call<MealNetworkWrapper> call = mealService.getMealByName(name);
        call.enqueue(new Callback<MealNetworkWrapper>() {
            @Override
            public void onResponse(@NonNull Call<MealNetworkWrapper> call, @NonNull Response<MealNetworkWrapper> response) {
                assert response.body() != null;
                callback.onSuccess(response.body().getMeals());
            }

            @Override
            public void onFailure(@NonNull Call<MealNetworkWrapper> call, @NonNull Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    public void getMealsByCountry(String country,MealListNetworkCB callback){
        Call<MealNetworkWrapper> call = mealService.getMealsByCountry(country);
        call.enqueue(new Callback<MealNetworkWrapper>() {
            @Override
            public void onResponse(@NonNull Call<MealNetworkWrapper> call, @NonNull Response<MealNetworkWrapper> response) {
                assert response.body() != null;
                callback.onSuccess(response.body().getMeals());
            }

            @Override
            public void onFailure(@NonNull Call<MealNetworkWrapper> call, @NonNull Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    public void getMealsByIngredient(String ingredient,MealListNetworkCB callback){
        Call<MealNetworkWrapper> call = mealService.getMealsByIngredient(ingredient);
        call.enqueue(new Callback<MealNetworkWrapper>() {
            @Override
            public void onResponse(@NonNull Call<MealNetworkWrapper> call, @NonNull Response<MealNetworkWrapper> response) {
                assert response.body() != null;
                callback.onSuccess(response.body().getMeals());
            }

            @Override
            public void onFailure(@NonNull Call<MealNetworkWrapper> call, @NonNull Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    public void getMealsByCategory(String category,MealListNetworkCB callback){
        Call<MealNetworkWrapper> call = mealService.getMealsByCategory(category);
        call.enqueue(new Callback<MealNetworkWrapper>() {
            @Override
            public void onResponse(@NonNull Call<MealNetworkWrapper> call, @NonNull Response<MealNetworkWrapper> response) {
                assert response.body() != null;
                callback.onSuccess(response.body().getMeals());
            }

            @Override
            public void onFailure(@NonNull Call<MealNetworkWrapper> call, @NonNull Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    public void getCategories(CategoriesNetworkCB callback){
        Call<CategoryNetworkWrapper> call = mealService.getCategories();
        call.enqueue(new Callback<CategoryNetworkWrapper>() {
            @Override
            public void onResponse(@NonNull Call<CategoryNetworkWrapper> call, @NonNull Response<CategoryNetworkWrapper> response) {
                assert response.body() != null;
                callback.onSuccess(response.body().getCategories());
            }

            @Override
            public void onFailure(@NonNull Call<CategoryNetworkWrapper> call, @NonNull Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    public void getCountries(CountriesNetworkCB callback){
        Call<CountryNetworkWrapper> call = mealService.getCountries();
        call.enqueue(new Callback<CountryNetworkWrapper>() {
            @Override
            public void onResponse(@NonNull Call<CountryNetworkWrapper> call, @NonNull Response<CountryNetworkWrapper> response) {
                assert response.body() != null;
                callback.onSuccess(response.body().getCountries());
            }

            @Override
            public void onFailure(@NonNull Call<CountryNetworkWrapper> call, @NonNull Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

}
