package com.example.androidprojectjava_foodplanner.remote.meal;

import com.example.androidprojectjava_foodplanner.model.pojo.Ingredient;

import java.util.List;

public interface IngredientNetworkCB {
    public void onSuccess(List<Ingredient> ingredientList);
    public void onFailure(String message);

}
