package com.example.androidprojectjava_foodplanner.remote;

import com.example.androidprojectjava_foodplanner.model.Country;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CountryNetworkWrapper {
    @SerializedName("meals")
    private List<Country> countries;

    public List<Country> getCountries() {
        return countries;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }
}
