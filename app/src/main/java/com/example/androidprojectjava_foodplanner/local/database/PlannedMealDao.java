package com.example.androidprojectjava_foodplanner.local.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.androidprojectjava_foodplanner.model.pojo.PlannedMeal;

import java.util.List;

@Dao
public interface PlannedMealDao {
    @Query("SELECT * FROM PlannedMealsTable")
    LiveData<List<PlannedMeal>> getAllPlannedMeals();
    @Insert
    void insertPlannedMeal(PlannedMeal plannedMeal);
    @Delete
    void deletePlannedMeal(PlannedMeal plannedMeal);

    @Query("SELECT * FROM PLANNEDMEALSTABLE where date = :date LIMIT 1")
    PlannedMeal getPlannedMealByDate(String date);

    @Query("DELETE FROM PLANNEDMEALSTABLE where date = :date")
    void deletePlannedMealByDate(String date);

    @Query("SELECT * FROM PLANNEDMEALSTABLE where id = :id LIMIT 1")
    PlannedMeal getPlannedMealByID(int id);

    @Query("DELETE FROM PLANNEDMEALSTABLE where id = :id")
    void deletePlannedMealByID(int id);

    @Query("SELECT * FROM PLANNEDMEALSTABLE where day = :day and month = :month and year = :year LIMIT 1")
    PlannedMeal getPlannedMealByDate(int day, int month, int year);

    @Query("DELETE FROM PLANNEDMEALSTABLE where day = :day and month = :month and year = :year")
    void deletePlannedMealByDate(int day, int month, int year);

    @Query("SELECT * FROM PLANNEDMEALSTABLE where meal_id = :meal_id LIMIT 1")
    PlannedMeal getPlannedMealByMealID(int meal_id);

    @Query("DELETE FROM PLANNEDMEALSTABLE where meal_id = :meal_id")
    void deletePlannedMealByMealID(int meal_id);

    @Query("SELECT * FROM PLANNEDMEALSTABLE where meal_name = :meal_name LIMIT 1")
    PlannedMeal getPlannedMealByMealName(String meal_name);

    @Query("DELETE FROM PLANNEDMEALSTABLE where meal_name = :meal_name")
    void deletePlannedMealByMealName(String meal_name);

    @Query("DELETE FROM PLANNEDMEALSTABLE")
    void deleteAllPlannedMeals();

}
