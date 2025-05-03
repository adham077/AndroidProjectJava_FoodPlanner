package com.example.androidprojectjava_foodplanner.model.pojo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
public class Meal implements Serializable {
    @SerializedName("idMeal")
    protected int id;

    @SerializedName("strMeal")
    protected String name;
    @SerializedName("strMealThumb")
    protected String imageUrl;
    @SerializedName("strArea")
    protected String country;
    @SerializedName("strIngredient1")
    protected String ingredient1;
    @SerializedName("strIngredient2")
    protected String ingredient2;
    @SerializedName("strIngredient3")
    protected String ingredient3;
    @SerializedName("strIngredient4")
    protected String ingredient4;
    @SerializedName("strIngredient5")
    protected String ingredient5;
    @SerializedName("strIngredient6")
    protected String ingredient6;
    @SerializedName("strIngredient7")
    protected String ingredient7;
    @SerializedName("strIngredient8")
    protected String ingredient8;
    @SerializedName("strIngredient9")
    protected String ingredient9;
    @SerializedName("strIngredient10")
    protected String ingredient10;
    @SerializedName("strIngredient11")
    protected String ingredient11;
    @SerializedName("strIngredient12")
    protected String ingredient12;
    @SerializedName("strIngredient13")
    protected String ingredient13;
    @SerializedName("strIngredient14")
    protected String ingredient14;
    @SerializedName("strIngredient15")
    protected String ingredient15;
    @SerializedName("strIngredient16")
    protected String ingredient16;
    @SerializedName("strIngredient17")
    protected String ingredient17;
    @SerializedName("strIngredient18")
    protected String ingredient18;
    @SerializedName("strIngredient19")
    protected String ingredient19;
    @SerializedName("strIngredient20")
    protected String ingredient20;
    @SerializedName("strMeasure1")
    protected String measurement1;
    @SerializedName("strMeasure2")
    protected String measurement2;
    @SerializedName("strMeasure3")
    protected String measurement3;
    @SerializedName("strMeasure4")
    protected String measurement4;
    @SerializedName("strMeasure5")
    protected String measurement5;
    @SerializedName("strMeasure6")
    protected String measurement6;
    @SerializedName("strMeasure7")
    protected String measurement7;
    @SerializedName("strMeasure8")
    protected String measurement8;
    @SerializedName("strMeasure9")
    protected String measurement9;
    @SerializedName("strMeasure10")
    protected String measurement10;
    @SerializedName("strMeasure11")
    protected String measurement11;
    @SerializedName("strMeasure12")
    protected String measurement12;
    @SerializedName("strMeasure13")
    protected String measurement13;
    @SerializedName("strMeasure14")
    protected String measurement14;
    @SerializedName("strMeasure15")
    protected String measurement15;
    @SerializedName("strMeasure16")
    protected String measurement16;
    @SerializedName("strMeasure17")
    protected String measurement17;
    @SerializedName("strMeasure18")
    protected String measurement18;
    @SerializedName("strMeasure19")
    protected String measurement19;
    @SerializedName("strMeasure20")
    protected String measurement20;
    @SerializedName("strInstructions")
    protected String instructions;
    @SerializedName("strYoutube")
    protected String videoUrl;

    protected transient boolean isFavourited;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getIngredient1() {
        return ingredient1;
    }

    public void setIngredient1(String ingredient1) {
        this.ingredient1 = ingredient1;
    }

    public String getIngredient2() {
        return ingredient2;
    }

    public void setIngredient2(String ingredient2) {
        this.ingredient2 = ingredient2;
    }

    public String getIngredient3() {
        return ingredient3;
    }

    public void setIngredient3(String ingredient3) {
        this.ingredient3 = ingredient3;
    }

    public String getIngredient4() {
        return ingredient4;
    }

    public void setIngredient4(String ingredient4) {
        this.ingredient4 = ingredient4;
    }

    public String getIngredient5() {
        return ingredient5;
    }

    public void setIngredient5(String ingredient5) {
        this.ingredient5 = ingredient5;
    }

    public String getIngredient6() {
        return ingredient6;
    }

    public void setIngredient6(String ingredient6) {
        this.ingredient6 = ingredient6;
    }

    public String getIngredient7() {
        return ingredient7;
    }

    public void setIngredient7(String ingredient7) {
        this.ingredient7 = ingredient7;
    }

    public String getIngredient8() {
        return ingredient8;
    }

    public void setIngredient8(String ingredient8) {
        this.ingredient8 = ingredient8;
    }

    public String getIngredient9() {
        return ingredient9;
    }

    public void setIngredient9(String ingredient9) {
        this.ingredient9 = ingredient9;
    }

    public String getIngredient10() {
        return ingredient10;
    }

    public void setIngredient10(String ingredient10) {
        this.ingredient10 = ingredient10;
    }

    public String getIngredient11() {
        return ingredient11;
    }

    public void setIngredient11(String ingredient11) {
        this.ingredient11 = ingredient11;
    }

    public String getIngredient12() {
        return ingredient12;
    }

    public void setIngredient12(String ingredient12) {
        this.ingredient12 = ingredient12;
    }

    public String getIngredient13() {
        return ingredient13;
    }

    public void setIngredient13(String ingredient13) {
        this.ingredient13 = ingredient13;
    }

    public String getIngredient14() {
        return ingredient14;
    }

    public void setIngredient14(String ingredient14) {
        this.ingredient14 = ingredient14;
    }

    public String getIngredient15() {
        return ingredient15;
    }

    public void setIngredient15(String ingredient15) {
        this.ingredient15 = ingredient15;
    }

    public String getIngredient16() {
        return ingredient16;
    }

    public void setIngredient16(String ingredient16) {
        this.ingredient16 = ingredient16;
    }

    public String getIngredient17() {
        return ingredient17;
    }

    public void setIngredient17(String ingredient17) {
        this.ingredient17 = ingredient17;
    }

    public String getIngredient18() {
        return ingredient18;
    }

    public void setIngredient18(String ingredient18) {
        this.ingredient18 = ingredient18;
    }

    public String getIngredient19() {
        return ingredient19;
    }

    public void setIngredient19(String ingredient19) {
        this.ingredient19 = ingredient19;
    }

    public String getIngredient20() {
        return ingredient20;
    }

    public void setIngredient20(String ingredient20) {
        this.ingredient20 = ingredient20;
    }

    public String getMeasurement1() {
        return measurement1;
    }

    public void setMeasurement1(String measurement1) {
        this.measurement1 = measurement1;
    }

    public String getMeasurement2() {
        return measurement2;
    }

    public void setMeasurement2(String measurement2) {
        this.measurement2 = measurement2;
    }

    public String getMeasurement3() {
        return measurement3;
    }

    public void setMeasurement3(String measurement3) {
        this.measurement3 = measurement3;
    }

    public String getMeasurement4() {
        return measurement4;
    }

    public void setMeasurement4(String measurement4) {
        this.measurement4 = measurement4;
    }

    public String getMeasurement5() {
        return measurement5;
    }

    public void setMeasurement5(String measurement5) {
        this.measurement5 = measurement5;
    }

    public String getMeasurement6() {
        return measurement6;
    }

    public void setMeasurement6(String measurement6) {
        this.measurement6 = measurement6;
    }

    public String getMeasurement7() {
        return measurement7;
    }

    public void setMeasurement7(String measurement7) {
        this.measurement7 = measurement7;
    }

    public String getMeasurement8() {
        return measurement8;
    }

    public void setMeasurement8(String measurement8) {
        this.measurement8 = measurement8;
    }

    public String getMeasurement9() {
        return measurement9;
    }

    public void setMeasurement9(String measurement9) {
        this.measurement9 = measurement9;
    }

    public String getMeasurement10() {
        return measurement10;
    }

    public void setMeasurement10(String measurement10) {
        this.measurement10 = measurement10;
    }

    public String getMeasurement11() {
        return measurement11;
    }

    public void setMeasurement11(String measurement11) {
        this.measurement11 = measurement11;
    }

    public String getMeasurement12() {
        return measurement12;
    }

    public void setMeasurement12(String measurement12) {
        this.measurement12 = measurement12;
    }

    public String getMeasurement13() {
        return measurement13;
    }

    public void setMeasurement13(String measurement13) {
        this.measurement13 = measurement13;
    }

    public String getMeasurement14() {
        return measurement14;
    }

    public void setMeasurement14(String measurement14) {
        this.measurement14 = measurement14;
    }

    public String getMeasurement15() {
        return measurement15;
    }

    public void setMeasurement15(String measurement15) {
        this.measurement15 = measurement15;
    }

    public String getMeasurement16() {
        return measurement16;
    }

    public void setMeasurement16(String measurement16) {
        this.measurement16 = measurement16;
    }

    public String getMeasurement17() {
        return measurement17;
    }

    public void setMeasurement17(String measurement17) {
        this.measurement17 = measurement17;
    }

    public String getMeasurement18() {
        return measurement18;
    }

    public void setMeasurement18(String measurement18) {
        this.measurement18 = measurement18;
    }

    public String getMeasurement19() {
        return measurement19;
    }

    public void setMeasurement19(String measurement19) {
        this.measurement19 = measurement19;
    }

    public String getMeasurement20() {
        return measurement20;
    }

    public void setMeasurement20(String measurement20) {
        this.measurement20 = measurement20;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public boolean isFavourited() {
        return isFavourited;
    }

    public void setFavourited(boolean favourited) {
        isFavourited = favourited;
    }

    public List<String>getIngredients(){
        List<String> ingredients = new ArrayList<String>(0);
        if((ingredient1 != null) && (!ingredient1.isEmpty())){
            ingredients.add(ingredient1);
        }
        if((ingredient2 != null) && (!ingredient2.isEmpty())){
            ingredients.add(ingredient2);
        }
        if((ingredient3 != null) && (!ingredient3.isEmpty())){
            ingredients.add(ingredient3);
        }
        if((ingredient4 != null) && (!ingredient4.isEmpty())){
            ingredients.add(ingredient4);
        }
        if((ingredient5 != null) && (!ingredient5.isEmpty())){
            ingredients.add(ingredient5);
        }
        if((ingredient6 != null) && (!ingredient6.isEmpty())){
            ingredients.add(ingredient6);
        }
        if((ingredient7 != null) && (!ingredient7.isEmpty())){
            ingredients.add(ingredient7);
        }
        if((ingredient8 != null) && (!ingredient8.isEmpty())){
            ingredients.add(ingredient8);
        }
        if((ingredient9 != null) && (!ingredient9.isEmpty())){
            ingredients.add(ingredient9);
        }
        if((ingredient10 != null) && (!ingredient10.isEmpty())){
            ingredients.add(ingredient10);
        }
        if((ingredient11 != null) && (!ingredient11.isEmpty())){
            ingredients.add(ingredient11);
        }
        if((ingredient12 != null) && (!ingredient12.isEmpty())){
            ingredients.add(ingredient12);
        }
        if((ingredient13 != null) && (!ingredient13.isEmpty())){
            ingredients.add(ingredient13);
        }
        if((ingredient14 != null) && (!ingredient14.isEmpty())){
            ingredients.add(ingredient14);
        }
        if((ingredient15 != null) && (!ingredient15.isEmpty())){
            ingredients.add(ingredient15);
        }
        if((ingredient16 != null) && (!ingredient16.isEmpty())){
            ingredients.add(ingredient16);
        }
        if((ingredient17 != null) && (!ingredient17.isEmpty())){
            ingredients.add(ingredient17);
        }
        if((ingredient18 != null) && (!ingredient18.isEmpty())){
            ingredients.add(ingredient18);
        }
        if((ingredient19 != null) && (!ingredient19.isEmpty())){
            ingredients.add(ingredient19);
        }
        if((ingredient20 != null) && (!ingredient20.isEmpty())){
            ingredients.add(ingredient20);
        }
        return ingredients;
    }

    public List<String>getMeasurements(){
        List<String> measurements = new ArrayList<String>(0);
        if((measurement1 != null) && (!measurement1.isEmpty())){
            measurements.add(measurement1);
        }
        if((measurement2 != null) && (!measurement2.isEmpty())){
            measurements.add(measurement2);
        }
        if((measurement3 != null) && (!measurement3.isEmpty())){
            measurements.add(measurement3);
        }
        if((measurement4 != null) && (!measurement4.isEmpty())){
            measurements.add(measurement4);
        }
        if((measurement5 != null) && (!measurement5.isEmpty())){
            measurements.add(measurement5);
        }
        if((measurement6 != null) && (!measurement6.isEmpty())){
            measurements.add(measurement6);
        }
        if((measurement7 != null) && (!measurement7.isEmpty())){
            measurements.add(measurement7);
        }
        if((measurement8 != null) && (!measurement8.isEmpty())){
            measurements.add(measurement8);
        }
        if((measurement9 != null) && (!measurement9.isEmpty())){
            measurements.add(measurement9);
        }
        if((measurement10 != null) && (!measurement10.isEmpty())){
            measurements.add(measurement10);
        }
        if((measurement11 != null) && (!measurement11.isEmpty())){
            measurements.add(measurement11);
        }
        if((measurement12 != null) && (!measurement12.isEmpty())){
            measurements.add(measurement12);
        }
        if((measurement13 != null) && (!measurement13.isEmpty())){
            measurements.add(measurement13);
        }
        if((measurement14 != null) && (!measurement14.isEmpty())){
            measurements.add(measurement14);
        }
        if((measurement15 != null) && (!measurement15.isEmpty())){
            measurements.add(measurement15);
        }
        if((measurement16 != null) && (!measurement16.isEmpty())){
            measurements.add(measurement16);
        }
        if((measurement17 != null) && (!measurement17.isEmpty())){
            measurements.add(measurement17);
        }
        if((measurement18 != null) && (!measurement18.isEmpty())){
            measurements.add(measurement18);
        }
        if((measurement19 != null) && (!measurement19.isEmpty())){
            measurements.add(measurement19);
        }
        if((measurement20 != null) && (!measurement20.isEmpty())){
            measurements.add(measurement20);
        }
        return measurements;
    }
}
