package com.example.androidprojectjava_foodplanner.model.pojo;

import java.util.List;

public class PlannerUser {
    private String id;
    private String name;
    private String email;
    private List<FavouriteMeal>favouriteMeals;
    private List<PlannedMeal>plannedMeals;
    public PlannerUser(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<FavouriteMeal> getFavouriteMeals() {
        return favouriteMeals;
    }

    public void setFavouriteMeals(List<FavouriteMeal> favouriteMeals) {
        this.favouriteMeals = favouriteMeals;
    }

    public List<PlannedMeal> getPlannedMeals() {
        return plannedMeals;
    }

    public void setPlannedMeals(List<PlannedMeal> plannedMeals) {
        this.plannedMeals = plannedMeals;
    }
}
