package com.example.androidprojectjava_foodplanner.AppNavigationView.favourite.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.androidprojectjava_foodplanner.AppNavigationView.details.view.MealDetailsActivity;
import com.example.androidprojectjava_foodplanner.AppNavigationView.favourite.presenter.FavPresenter;
import com.example.androidprojectjava_foodplanner.R;
import com.example.androidprojectjava_foodplanner.model.pojo.Meal;

import java.util.List;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder> {

    private List<Meal> mealList;
    private List<Bitmap> mealImages;
    private FavPresenter presenter;
    public FavouriteAdapter(List<Meal> mealList, List<Bitmap> mealImages,FavPresenter presenter){
        this.mealList = mealList;
        this.mealImages = mealImages;
        this.presenter = presenter;
    }
    public void setMealList(List<Meal> mealList) {
        this.mealList = mealList;
    }

    @NonNull
    @Override
    public FavouriteAdapter.FavouriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_favourite,parent,false);
        return new FavouriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteAdapter.FavouriteViewHolder holder, int position) {
        Meal meal = mealList.get(position);
        holder.textView.setText(meal.getName());
        Glide.with(holder.imageView.getContext()).load(mealImages.get(position)).into(holder.imageView);

        final int pos = position;

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MealDetailsActivity.class);
                intent.putExtra("mealId",meal.getId());
                intent.putExtra("senderID","FavouriteFragment");
                v.getContext().startActivity(intent);
            }
        });

        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION && pos < mealList.size()) {
                    Meal mealToRemove = mealList.get(pos);
                    presenter.onDeleteClicked(mealToRemove);
                    mealList.remove(pos);
                    mealImages.remove(pos);
                    notifyItemRemoved(pos);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mealList != null) return mealList.size();
        else return 0;
    }

    public class FavouriteViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageView;
        private TextView textView;
        private ImageButton imageButton;
        private CardView cardView;
        public FavouriteViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.mealImageFav);
            this.textView = itemView.findViewById(R.id.mealNameFav);
            this.imageButton = itemView.findViewById(R.id.removeButton);
            this.cardView = itemView.findViewById(R.id.cardViewFav);
        }
    }
}
