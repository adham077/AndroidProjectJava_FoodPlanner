package com.example.androidprojectjava_foodplanner.remote.meal;

import com.example.androidprojectjava_foodplanner.model.pojo.Ingredient;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class IngredientNetworkWrapper {
    @SerializedName("meals")
    private List<Ingredient>ingredientList;

    public List<Ingredient> getIngredientList() {
        return ingredientList;
    }

    public void setIngredientList(List<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
    }
}
