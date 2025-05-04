package com.example.androidprojectjava_foodplanner.remote.meal;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MealNetworkService {
    @GET("lookup.php")
    Call<MealNetworkWrapper>getMealById(@Query("i") int id);

    @GET("random.php")
    Call<MealNetworkWrapper>getRandomMeal();

    @GET("search.php")
    Call<MealNetworkWrapper>getMealByName(@Query("s") String name);

    @GET("filter.php")
    Call<MealNetworkWrapper>getMealsByCountry(@Query("a") String country);

    @GET("filter.php")
    Call<MealNetworkWrapper>getMealsByIngredient(@Query("i") String ingredient);

    @GET("filter.php")
    Call<MealNetworkWrapper>getMealsByCategory(@Query("c") String category);

    @GET("categories.php")
    Call<CategoryNetworkWrapper>getCategories();
    @GET("list.php?a=list")
    Call<CountryNetworkWrapper>getCountries();

    @GET("list.php?i=list")
    Call<IngredientNetworkWrapper>getIngredients();


}
