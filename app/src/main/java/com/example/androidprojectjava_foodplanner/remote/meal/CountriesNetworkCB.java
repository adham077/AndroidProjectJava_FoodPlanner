package com.example.androidprojectjava_foodplanner.remote.meal;

import com.example.androidprojectjava_foodplanner.model.Country;

import java.util.List;

public interface CountriesNetworkCB {
    public void onSuccess(List<Country> countries);
    public void onFailure(String msg);
}
