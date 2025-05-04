package com.example.androidprojectjava_foodplanner.AppNavigationView.search.view;

import com.example.androidprojectjava_foodplanner.model.pojo.Category;
import com.example.androidprojectjava_foodplanner.model.pojo.Country;
import com.example.androidprojectjava_foodplanner.model.pojo.Ingredient;
import com.example.androidprojectjava_foodplanner.model.pojo.Meal;

import java.util.List;

public interface SearchContract {
    void updateRecyclerWithCategories(List<Category> categories);
    void updateRecyclerWithIngredients(List<Ingredient> ingredients);
    void updateRecyclerWithCountries(List<Country> countries);
    void updateRecyclerWithMeals(List<Meal> mealList);


}
