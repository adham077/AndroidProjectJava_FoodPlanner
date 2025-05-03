package com.example.androidprojectjava_foodplanner.remote.meal.ingredients;

import android.graphics.Bitmap;

import java.util.List;

public interface IngredientsNetworkCB {
    public void onSuccess(List<Bitmap> imagesBitmap);
    public void onSuccess(Bitmap imageBitmap);
    public void onFailure(String message);
}
