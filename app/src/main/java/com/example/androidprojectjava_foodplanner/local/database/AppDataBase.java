package com.example.androidprojectjava_foodplanner.local.database;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.androidprojectjava_foodplanner.model.pojo.FavouriteMeal;
import com.example.androidprojectjava_foodplanner.model.pojo.PlannedMeal;

@Database(entities = {FavouriteMeal.class, PlannedMeal.class},version = 2)
public abstract class AppDataBase extends RoomDatabase{
    private static AppDataBase instance = null;
    public abstract FavouriteMealDao getFavouriteMealDao();
    public abstract PlannedMealDao getPlannedMealDao();

    public static synchronized AppDataBase getInstance(Object context){
        if(instance == null){
            instance = Room.databaseBuilder(
                            (Context) context,
                            AppDataBase.class,"MyFoodPlanner.db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        instance.getOpenHelper().getWritableDatabase();
        Log.i("DataBase","Instance Created");
        return instance;
    }
}
