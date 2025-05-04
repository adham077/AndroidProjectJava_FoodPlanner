package com.example.androidprojectjava_foodplanner.AppNavigationView.search.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.androidprojectjava_foodplanner.AppNavigationView.search.presenter.SearchPresenter;
import com.example.androidprojectjava_foodplanner.R;
import com.example.androidprojectjava_foodplanner.local.database.MealLocalDataSource;
import com.example.androidprojectjava_foodplanner.model.pojo.Category;
import com.example.androidprojectjava_foodplanner.model.pojo.Country;
import com.example.androidprojectjava_foodplanner.model.pojo.Ingredient;
import com.example.androidprojectjava_foodplanner.model.pojo.Meal;
import com.example.androidprojectjava_foodplanner.model.repository.MealRepository;
import com.example.androidprojectjava_foodplanner.remote.meal.MealRemoteDataSource;
import com.example.androidprojectjava_foodplanner.remote.meal.ingredients.IngredientsRemoteDataSource;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.search.SearchView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;
import java.util.stream.Collectors;

public class SearchFragment extends Fragment implements SearchContract{
    private ChipGroup chipGroup;
    private Chip chipIngredients, chipCategories, chipCountries;
    private TextInputEditText searchEditText;
    private RecyclerView searchRecycler;
    private SearchAdapter searchAdapter;
    private List<Category> categories;
    private List<Ingredient> ingredients;
    private List<Country> countries;
    private List<Meal> meals;
    private SearchView searchView;


    private SearchPresenter searchPresenter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        chipIngredients = view.findViewById(R.id.chipIngredients);
        chipCategories = view.findViewById(R.id.chipCategories);
        chipCountries = view.findViewById(R.id.chipCountries);
        searchRecycler = view.findViewById(R.id.searchRecycler);
        searchEditText = view.findViewById(R.id.searchEditText);
        chipGroup = view.findViewById(R.id.chipGroupid);

        MealRepository mealRepository = MealRepository.getInstance(
                MealRemoteDataSource.getInstance(this.getContext()),
                MealLocalDataSource.getInstance(this.getContext())
        );

        IngredientsRemoteDataSource ingredientsRemoteDataSource = IngredientsRemoteDataSource.getInstance(this.getContext());
        searchPresenter = SearchPresenter.getInstance(mealRepository, ingredientsRemoteDataSource,this);

        ChipGroup chipGroup = view.findViewById(R.id.chipGroupid);
        chipGroup.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {
                if (checkedIds.isEmpty()) return;
                int selectedId = checkedIds.get(0);
                if (selectedId == R.id.chipIngredients) {
                    searchPresenter.getAllIngredients();
                } else if (selectedId == R.id.chipCategories) {
                    Log.i("SearchFragmentChipSelected", "Selected Categories");
                    searchPresenter.getAllCategories();
                } else if (selectedId == R.id.chipCountries) {
                    searchPresenter.getAllCountries();
                }
                else{
                    Log.i("SearchFragmentChipSelected", "Unknown chip selected: " + selectedId);
                }
            }
        });

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString();
                if (chipIngredients.isChecked()) {
                    filterIngredients(ingredients, query);
                } else if (chipCategories.isChecked()) {
                    filterCategories(categories, query);
                } else if (chipCountries.isChecked()) {
                    filterCountries(countries,query);
                } else {
                    if(meals == null){
                        Log.i("SearchMeals" ,"NUll");

                    }
                    else{
                        Log.i("SearchMeals","Not null");
                        filterMeals(meals,query);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void filterIngredients(List<Ingredient> sourceList, String query){
        List<Ingredient> filtered = sourceList.stream()
                .filter(item -> item.getName().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
        searchAdapter.updateList(filtered,SearchAdapter.SearchType.INGREDIENTS);
    }
    private void filterCategories(List<Category> sourceList, String query){
        List<Category> filtered = sourceList.stream()
                .filter(item -> item.getName().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
        searchAdapter.updateList(filtered,SearchAdapter.SearchType.CATEGORIES);
    }
    private void filterCountries(List<Country> sourceList,String query){
        List<Country> filtered = sourceList.stream()
                .filter(item -> item.getName().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
        searchAdapter.updateList(filtered,SearchAdapter.SearchType.CATEGORIES);
    }

    private void filterMeals(List<Meal> sourceList, String query){

        List<Meal> filtered = sourceList.stream()
                .filter(item -> item.getName().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
        searchAdapter.updateList(filtered,SearchAdapter.SearchType.MEALS);
    }

    @Override
    public void updateRecyclerWithCategories(List<Category> categories) {
        this.categories = categories;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("CategoriesFound", categories.toString());
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                        SearchFragment.this.getContext(),
                        LinearLayoutManager.VERTICAL,
                        false
                );
                searchRecycler.setLayoutManager(linearLayoutManager);
                searchAdapter = new SearchAdapter(SearchAdapter.SearchType.CATEGORIES,categories);
                searchAdapter.setPresenter(SearchFragment.this.searchPresenter);
                searchRecycler.setAdapter(searchAdapter);
            }
        });
    }

    @Override
    public void updateRecyclerWithIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                        SearchFragment.this.getContext(),
                        LinearLayoutManager.VERTICAL,
                        false
                );
                searchRecycler.setLayoutManager(linearLayoutManager);
                searchAdapter = new SearchAdapter(SearchAdapter.SearchType.INGREDIENTS,ingredients);
                searchAdapter.setPresenter(SearchFragment.this.searchPresenter);
                searchRecycler.setAdapter(searchAdapter);
            }
        });
    }

    @Override
    public void updateRecyclerWithCountries(List<Country> countries) {

    }

    @Override
    public void updateRecyclerWithMeals(List<Meal> mealList){
        this.meals = mealList;
        chipGroup.clearCheck();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                        SearchFragment.this.getContext(),
                        LinearLayoutManager.VERTICAL,
                        false
                );
                searchRecycler.setLayoutManager(linearLayoutManager);
                searchAdapter = new SearchAdapter(SearchAdapter.SearchType.MEALS,mealList);
                searchAdapter.setPresenter(SearchFragment.this.searchPresenter);
                searchRecycler.setAdapter(searchAdapter);
            }
        });
    }
}