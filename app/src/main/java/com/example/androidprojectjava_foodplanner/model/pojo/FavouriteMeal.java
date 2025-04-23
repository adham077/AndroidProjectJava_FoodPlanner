package com.example.androidprojectjava_foodplanner.model.pojo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "FavouriteMealsTable")
public class FavouriteMeal extends Meal{
    @PrimaryKey(autoGenerate = true)
    private int favMealId;

    public FavouriteMeal(){

    }

    public FavouriteMeal(Meal meal){
        this.id= meal.id;
        this.name= meal.name;
        this.imageUrl= meal.imageUrl;
        this.country= meal.country;
        this.ingredient1= meal.ingredient1;
        this.ingredient2= meal.ingredient2;
        this.ingredient3= meal.ingredient3;
        this.ingredient4= meal.ingredient4;
        this.ingredient5= meal.ingredient5;
        this.ingredient6= meal.ingredient6;
        this.ingredient7= meal.ingredient7;
        this.ingredient8= meal.ingredient8;
        this.ingredient9= meal.ingredient9;
        this.ingredient10= meal.ingredient10;
        this.ingredient11= meal.ingredient11;
        this.ingredient12= meal.ingredient12;
        this.ingredient13= meal.ingredient13;
        this.ingredient14= meal.ingredient14;
        this.ingredient15= meal.ingredient15;
        this.ingredient16= meal.ingredient16;
        this.ingredient17= meal.ingredient17;
        this.ingredient18= meal.ingredient18;
        this.ingredient19= meal.ingredient19;
        this.ingredient20= meal.ingredient20;
        this.measurement1= meal.measurement1;
        this.measurement2= meal.measurement2;
        this.measurement3= meal.measurement3;
        this.measurement4= meal.measurement4;
        this.measurement5= meal.measurement5;
        this.measurement6= meal.measurement6;
        this.measurement7= meal.measurement7;
        this.measurement8= meal.measurement8;
        this.measurement9= meal.measurement9;
        this.measurement10= meal.measurement10;
        this.measurement11= meal.measurement11;
        this.measurement12= meal.measurement12;
        this.measurement13= meal.measurement13;
        this.measurement14= meal.measurement14;
        this.measurement15= meal.measurement15;
        this.measurement16= meal.measurement16;
        this.measurement17= meal.measurement17;
        this.measurement18= meal.measurement18;
        this.measurement19= meal.measurement19;
        this.measurement20= meal.measurement20;
        this.instructions= meal.instructions;
        this.videoUrl= meal.videoUrl;
        this.isFavourited= meal.isFavourited;
    }
    public int getFavMealId() {
        return favMealId;
    }

    public void setFavMealId(int favMealId) {
        this.favMealId = favMealId;
    }
}
