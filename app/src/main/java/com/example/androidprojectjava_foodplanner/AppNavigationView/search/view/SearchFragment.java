package com.example.androidprojectjava_foodplanner.AppNavigationView.search.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.androidprojectjava_foodplanner.AppNavigationView.search.presenter.SearchPresenter;
import com.example.androidprojectjava_foodplanner.R;
import com.example.androidprojectjava_foodplanner.local.database.MealLocalDataSource;
import com.example.androidprojectjava_foodplanner.model.pojo.Category;
import com.example.androidprojectjava_foodplanner.model.pojo.Country;
import com.example.androidprojectjava_foodplanner.model.pojo.Ingredient;
import com.example.androidprojectjava_foodplanner.model.repository.MealRepository;
import com.example.androidprojectjava_foodplanner.remote.meal.MealRemoteDataSource;
import com.example.androidprojectjava_foodplanner.remote.meal.ingredients.IngredientsRemoteDataSource;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.search.SearchBar;

import java.util.List;
import java.util.stream.Collectors;

public class SearchFragment extends Fragment implements SearchContract{
    private SearchBar searchBar;
    private Chip chipIngredients, chipCategories, chipCountries;
    private RecyclerView searchRecycler;
    private SearchAdapter searchAdapter;
    private List<Category> categories;
    private List<Ingredient> ingredients;
    private List<Country> countries;

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
        searchBar = view.findViewById(R.id.searchBar);
        chipIngredients = view.findViewById(R.id.chipIngredients);
        chipCategories = view.findViewById(R.id.chipCategories);
        chipCountries = view.findViewById(R.id.chipCountries);
        searchRecycler = view.findViewById(R.id.searchRecycler);

        MealRepository mealRepository = MealRepository.getInstance(
                MealRemoteDataSource.getInstance(this.getContext()),
                MealLocalDataSource.getInstance(this.getContext())
        );

        IngredientsRemoteDataSource ingredientsRemoteDataSource = IngredientsRemoteDataSource.getInstance(this.getContext());
        searchPresenter = SearchPresenter.getInstance(mealRepository, ingredientsRemoteDataSource);
        setupChips();
    }

    private void setupChips(){
        ChipGroup chipGroup = getView().findViewById(R.id.chipGroup);
        chipGroup.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {
                if (checkedIds.isEmpty()) return;

                int selectedId = checkedIds.get(0);

                if (selectedId == R.id.chipIngredients) {
                    searchPresenter.getAllIngredients();
                } else if (selectedId == R.id.chipCategories) {
                    searchPresenter.getAllCategories();
                } else if (selectedId == R.id.chipCountries) {
                    searchPresenter.getAllCountries();
                }
                else{
                    return;
                }
            }
        });
    }


    private void filterIngredients(List<Ingredient> sourceList, String query){
        List<Ingredient> filtered = sourceList.stream()
                .filter(item -> item.getName().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
        //adapter.updateList(filtered);
    }
    private void filterCategories(List<Category> sourceList, String query){
        List<Category> filtered = sourceList.stream()
                .filter(item -> item.getName().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
        //adapter.updateList(filtered);
    }
    private void filterCountries(){

    }

    @Override
    public void updateRecyclerWithCategories(List<Category> categories) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    @Override
    public void updateRecyclerWithIngredients(List<Ingredient> ingredients) {

    }

    @Override
    public void updateRecyclerWithCountries(List<Country> countries) {

    }
}