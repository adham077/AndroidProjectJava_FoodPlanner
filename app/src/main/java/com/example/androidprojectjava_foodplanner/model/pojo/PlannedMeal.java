package com.example.androidprojectjava_foodplanner.model.pojo;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "PlannedMealsTable")
public class PlannedMeal{

    private int id;
    @Embedded(prefix = "meal_")
    private Meal meal;

    private int day;
    private int month;
    private int year;
    @PrimaryKey
    @NonNull
    private String date;
    public PlannedMeal(Meal meal,int day,int month,int year){
        this.meal = meal;
        this.day = day;
        this.month = month;
        this.year = year;
        this.date = day + ":" + month + ":" + year;
    }

    public Meal getMeal() {
        return meal;
    }

    public void setMeal(Meal meal) {
        this.meal = meal;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public static String getFormattedDate(int day,int month,int year){
        return day + ":" + month + ":" + year;
    }
}
