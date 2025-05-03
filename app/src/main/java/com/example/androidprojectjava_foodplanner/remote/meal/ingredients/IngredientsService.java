package com.example.androidprojectjava_foodplanner.remote.meal.ingredients;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface IngredientsService {
    @GET
    Call<ResponseBody> getIngredientImage(@Url String imageEndPoint);
}
