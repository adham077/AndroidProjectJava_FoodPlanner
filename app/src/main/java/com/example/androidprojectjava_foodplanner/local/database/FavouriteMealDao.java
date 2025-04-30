package com.example.androidprojectjava_foodplanner.local.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.androidprojectjava_foodplanner.model.pojo.FavouriteMeal;

import java.util.List;
@Dao
public interface FavouriteMealDao {
    @Query("SELECT * FROM FavouriteMealsTable")
    List<FavouriteMeal> getAllFavouriteMeals();
    @Insert
    void insertFavouriteMeal(FavouriteMeal favouriteMeal);
    @Delete
    void deleteFavouriteMeal(FavouriteMeal favouriteMeal);

    @Query("SELECT * FROM FavouriteMealsTable where id = :id LIMIT 1")
    FavouriteMeal getFavouriteMealByMealID(int id);
    @Query("DELETE FROM FavouriteMealsTable where id = :id")
    void deleteFavouriteMealByMealID(int id);
    @Query("SELECT * FROM FavouriteMealsTable where name = :name LIMIT 1")
    FavouriteMeal getFavouriteMealByName(String name);

    @Query("DELETE FROM FavouriteMealsTable where name = :name")
    void deleteFavouriteMealByName(String name);

    @Query("DELETE FROM FavouriteMealsTable")
    void deleteAllFavouriteMeals();

}
