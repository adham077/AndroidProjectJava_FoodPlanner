package com.example.androidprojectjava_foodplanner.AppNavigationView.search.presenter;

import com.example.androidprojectjava_foodplanner.model.pojo.Category;
import com.example.androidprojectjava_foodplanner.model.repository.MealRepository;
import com.example.androidprojectjava_foodplanner.remote.meal.CategoriesNetworkCB;
import com.example.androidprojectjava_foodplanner.remote.meal.MealRemoteDataSource;
import com.example.androidprojectjava_foodplanner.remote.meal.ingredients.IngredientsRemoteDataSource;

import java.util.List;

public class SearchPresenter {

    private static SearchPresenter instance;
    private MealRepository mealRepository;
    private IngredientsRemoteDataSource ingredientsRemoteDataSource;
    private SearchPresenter(MealRepository mealRepository, IngredientsRemoteDataSource ingredientsRemoteDataSource){
        this.mealRepository = mealRepository;
        this.ingredientsRemoteDataSource = ingredientsRemoteDataSource;
    }

    public static SearchPresenter getInstance(MealRepository mealRepository,IngredientsRemoteDataSource ingredientsRemoteDataSource){
        if(instance == null){
            instance = new SearchPresenter(mealRepository,ingredientsRemoteDataSource);
        }
        return instance;
    }

    public void getAllIngredients(){

    }

    public void getAllCountries(){

    }
    public void getAllCategories(){
        mealRepository.getCategoriesRemote(new CategoriesNetworkCB() {
            @Override
            public void onSuccess(List<Category> categories) {

            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }

}
