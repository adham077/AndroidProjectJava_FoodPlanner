package com.example.androidprojectjava_foodplanner.AppNavigationView.search.view;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.androidprojectjava_foodplanner.AppNavigationView.details.view.IngredientsAdapter;
import com.example.androidprojectjava_foodplanner.AppNavigationView.details.view.MealDetailsActivity;
import com.example.androidprojectjava_foodplanner.AppNavigationView.search.presenter.SearchPresenter;
import com.example.androidprojectjava_foodplanner.R;
import com.example.androidprojectjava_foodplanner.model.pojo.Category;
import com.example.androidprojectjava_foodplanner.model.pojo.Country;
import com.example.androidprojectjava_foodplanner.model.pojo.Ingredient;
import com.example.androidprojectjava_foodplanner.model.pojo.Meal;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    public enum SearchType{
        INGREDIENTS,
        CATEGORIES,
        COUNTRIES,
        MEALS
    }

    private List<Category> categoryList;
    private List<Ingredient> ingredientList;
    private List<Country> countryList;
    private List<Meal> mealList;
    private SearchType type;
    private SearchPresenter presenter;

    public SearchAdapter(SearchType type,Object searchItems){
        this.type = type;
        if(type == SearchType.INGREDIENTS){
            ingredientList = (List<Ingredient>) searchItems;
        }
        else if(type == SearchType.CATEGORIES){
            categoryList = (List<Category>) searchItems;
        }
        else if(type == SearchType.COUNTRIES){
            countryList = (List<Country>) searchItems;
        }
        else if(type == SearchType.MEALS){
            mealList = (List<Meal>) searchItems;
        }
        else{
            return;
        }
    }

    public void setPresenter(SearchPresenter presenter) {
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.search_item, parent, false);
        return  new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.SearchViewHolder holder, int position) {
        final int pos = position;
        switch (type){
            case INGREDIENTS:
                Ingredient ingredient = ingredientList.get(position);
                holder.textView.setText(ingredient.getName());
                Glide.with(holder.itemView.getContext()).asBitmap().load(ingredient.getImageBitmap()).into(holder.imageView);
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.getMealsByIngredient(ingredientList.get(pos));
                    }
                });
                break;

            case CATEGORIES:
                Category category = categoryList.get(position);
                holder.textView.setText(category.getName());
                Glide.with(holder.itemView.getContext()).asBitmap().load(category.getImageUrl()).into(holder.imageView);
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.getMealsByCategory(categoryList.get(pos));
                    }
                });
                break;

            case COUNTRIES:
                Country country = countryList.get(position);
                holder.textView.setText(country.getName());
                holder.imageView.setImageResource(country.getImageId());
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.getMealsByCountry(country);
                    }
                });
                break;

            case MEALS:
                Meal meal = mealList.get(position);
                holder.textView.setText(meal.getName());
                Glide.with(holder.itemView.getContext()).load(meal.getImageUrl()).into(holder.imageView);
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(holder.itemView.getContext(), MealDetailsActivity.class);
                        intent.putExtra("senderID","HomeFragment");
                        intent.putExtra("mealId",meal.getId());
                        holder.itemView.getContext().startActivity(intent);
                    }
                });
                break;

            default:
                break;
        }
    }

    public void updateList(Object list,SearchType type){
        if(type == SearchType.INGREDIENTS){
            ingredientList = (List<Ingredient>) list;
        }
        else if(type == SearchType.CATEGORIES){
            categoryList = (List<Category>) list;
            }
        else if(type == SearchType.COUNTRIES){
            countryList = (List<Country>) list;
        }
        else if(type == SearchType.MEALS){
            Log.i("SearchAdapterUpdateList","Updating Meals");
            mealList = (List<Meal>) list;
        }
        else{}
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(type == null)return 0;

        switch (type){
            case INGREDIENTS:
                return ingredientList.size();
            case CATEGORIES:
                return categoryList.size();
            case COUNTRIES:
                return countryList.size();
            case MEALS:
                return mealList.size();
            default:
                return 0;
        }
    }

    public static class SearchViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageView;
        public TextView textView;
        public CardView cardView;
        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.itemImage);
            textView = itemView.findViewById(R.id.itemText);
            cardView = itemView.findViewById(R.id.itemCard);
        }
    }
}
