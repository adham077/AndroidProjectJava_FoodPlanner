package com.example.androidprojectjava_foodplanner.AppNavigationView.search.presenter;

import android.graphics.Bitmap;
import android.util.Log;

import com.example.androidprojectjava_foodplanner.AppNavigationView.search.view.SearchContract;
import com.example.androidprojectjava_foodplanner.model.pojo.Category;
import com.example.androidprojectjava_foodplanner.model.pojo.Ingredient;
import com.example.androidprojectjava_foodplanner.model.pojo.Meal;
import com.example.androidprojectjava_foodplanner.model.repository.MealRepository;
import com.example.androidprojectjava_foodplanner.remote.meal.CategoriesNetworkCB;
import com.example.androidprojectjava_foodplanner.remote.meal.IngredientNetworkCB;
import com.example.androidprojectjava_foodplanner.remote.meal.MealListNetworkCB;
import com.example.androidprojectjava_foodplanner.remote.meal.MealRemoteDataSource;
import com.example.androidprojectjava_foodplanner.remote.meal.ingredients.IngredientsNetworkCB;
import com.example.androidprojectjava_foodplanner.remote.meal.ingredients.IngredientsRemoteDataSource;

import java.util.ArrayList;
import java.util.List;

public class SearchPresenter {

    private static SearchPresenter instance;
    private MealRepository mealRepository;
    private IngredientsRemoteDataSource ingredientsRemoteDataSource;
    private SearchContract view;
    private SearchPresenter(MealRepository mealRepository, IngredientsRemoteDataSource ingredientsRemoteDataSource,SearchContract view){
        this.mealRepository = mealRepository;
        this.ingredientsRemoteDataSource = ingredientsRemoteDataSource;
        this.view = view;
    }

    public static SearchPresenter getInstance(MealRepository mealRepository,IngredientsRemoteDataSource ingredientsRemoteDataSource,SearchContract view){
        if(instance == null){
            instance = new SearchPresenter(mealRepository,ingredientsRemoteDataSource,view);
        }
        return instance;
    }

    public void getAllIngredients(){
        mealRepository.getIngredients(new IngredientNetworkCB() {
            @Override
            public void onSuccess(List<Ingredient> ingredientList) {
                Log.d("Ingredients",ingredientList.toString());
                List<String> mealNames= new ArrayList<>();
                List<Ingredient> _ingredients = ingredientList;
                for(Ingredient ingredient : ingredientList){
                    mealNames.add(ingredient.getName());
                    Log.d("IngredientsImages",ingredient.getName());
                }
                ingredientsRemoteDataSource.getIngredientsImages(mealNames, new IngredientsNetworkCB() {
                    @Override
                    public void onSuccess(List<Bitmap> imagesBitmap) {
                        Log.d("IngredientsImages","bitmap fetched");

                        for(int i=0;i<imagesBitmap.size();i++){
                            _ingredients.get(i).setImageBitmap(imagesBitmap.get(i));
                        }
                        view.updateRecyclerWithIngredients(_ingredients);
                    }

                    @Override
                    public void onSuccess(Bitmap imageBitmap) {

                    }

                    @Override
                    public void onFailure(String message) {

                    }
                });
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }

    public void getAllCountries(){

    }
    public void getAllCategories(){
        mealRepository.getCategoriesRemote(new CategoriesNetworkCB() {
            @Override
            public void onSuccess(List<Category> categories) {
                view.updateRecyclerWithCategories(categories);
            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }

    public void getMealsByCategory(Category category){
        mealRepository.getMealsByCategoryRemote(category.getName(), new MealListNetworkCB() {
            @Override
            public void onSuccess(List<Meal> meals) {
                view.updateRecyclerWithMeals(meals);
            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }

    public void getMealsByIngredient(Ingredient ingredient){
        mealRepository.getMealsByIngredientRemote(ingredient.getName(), new MealListNetworkCB() {
            @Override
            public void onSuccess(List<Meal> meals) {
                view.updateRecyclerWithMeals(meals);
            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }

}
