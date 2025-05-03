package com.example.androidprojectjava_foodplanner.AppNavigationView.home.view;

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
import com.example.androidprojectjava_foodplanner.AppNavigationView.home.presenter.HomePresenter;
import com.example.androidprojectjava_foodplanner.R;
import com.example.androidprojectjava_foodplanner.model.pojo.Meal;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder>{

    private List<Meal> meals;
    private HomePresenter presenter;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        public ImageView imageView;
        public TextView mealTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.homeMealCardView);
            imageView = itemView.findViewById(R.id.homeMealImageView);
            mealTitle = itemView.findViewById(R.id.homeMealTextView);
        }
    }

    public HomeAdapter(List<Meal> meals,HomePresenter presenter) {
        this.meals = meals;
        this.presenter = presenter;
    }

    public void setPresenter(HomePresenter presenter){
        this.presenter = presenter;
    }

    public void setList(List<Meal> meals){
        this.meals = meals;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_meal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Meal meal = meals.get(position);
        String title = meal.getName();
        String imageUrl = meal.getImageUrl();
        holder.mealTitle.setText(title);

        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .into(holder.imageView);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.getMealDetails(meal.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        if(meals != null) return meals.size();
        return 0;
    }
}
