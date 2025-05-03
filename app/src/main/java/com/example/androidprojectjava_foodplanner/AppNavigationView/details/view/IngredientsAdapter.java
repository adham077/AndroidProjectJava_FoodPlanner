package com.example.androidprojectjava_foodplanner.AppNavigationView.details.view;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.androidprojectjava_foodplanner.R;
import com.example.androidprojectjava_foodplanner.model.pojo.Ingredient;

import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {
    private List<Ingredient> ingredientList;

    public IngredientsAdapter(List<Ingredient> ingredientList){
        Log.i("IngredientsAdapterList","started");
        this.ingredientList = ingredientList;
    }

    public void setIngredientList(List<Ingredient> ingredientList){
        this.ingredientList = ingredientList;
    }

    @NonNull
    @Override
    public IngredientsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_ingredient, parent, false);
        return new IngredientsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsAdapter.ViewHolder holder, int position) {
        Ingredient ingredient = ingredientList.get(position);
        holder.ingredient_name.setText(ingredient.getName());
        holder.ingredient_quantity.setText(ingredient.getMeasurement());
        Glide.with(holder.itemView.getContext()).asBitmap().load(ingredient.getImage()).into(holder.ingredientImage);
    }

    @Override
    public int getItemCount() {
        if(ingredientList!=null) return ingredientList.size();
        else return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ingredientImage;
        TextView ingredient_name;
        TextView ingredient_quantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientImage = itemView.findViewById(R.id.ingredientsImageView);
            ingredient_name = itemView.findViewById(R.id.ingredientsNameTextView);
            ingredient_quantity = itemView.findViewById(R.id.ingredientsMeasurementTextView);
        }
    }
}
