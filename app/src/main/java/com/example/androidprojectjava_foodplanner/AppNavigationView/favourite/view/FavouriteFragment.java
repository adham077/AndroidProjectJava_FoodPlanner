package com.example.androidprojectjava_foodplanner.AppNavigationView.favourite.view;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.androidprojectjava_foodplanner.AppNavigationView.favourite.presenter.FavPresenter;
import com.example.androidprojectjava_foodplanner.AppNavigationView.home.view.HomeAdapter;
import com.example.androidprojectjava_foodplanner.AppNavigationView.home.view.ItemSpacingDecoration;
import com.example.androidprojectjava_foodplanner.R;
import com.example.androidprojectjava_foodplanner.local.database.MealLocalDataSource;
import com.example.androidprojectjava_foodplanner.model.pojo.Meal;
import com.example.androidprojectjava_foodplanner.model.repository.MealRepository;
import com.example.androidprojectjava_foodplanner.model.repository.UserRepository;
import com.example.androidprojectjava_foodplanner.remote.meal.MealRemoteDataSource;
import com.example.androidprojectjava_foodplanner.remote.user.firebase.firebaseAuth.UserAuthentication;
import com.example.androidprojectjava_foodplanner.remote.user.firebase.firebaseDB.UserRemoteDataSource;

import java.util.List;



public class FavouriteFragment extends Fragment implements FavContract{

    private RecyclerView recyclerView;
    private FavPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favourite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.favoritesRecyclerView);
        MealLocalDataSource mealLocalDataSource = MealLocalDataSource.getInstance(this.getContext());
        MealRemoteDataSource mealRemoteDataSource = MealRemoteDataSource.getInstance(this.getContext());
        UserRepository userRepository = UserRepository.getInstance(
                mealLocalDataSource,
                mealRemoteDataSource,
                UserAuthentication.getInstance(),
                UserRemoteDataSource.getInstance(this.getContext())
        );

        MealRepository mealRepository = MealRepository.getInstance(mealRemoteDataSource,mealLocalDataSource);

        presenter = FavPresenter.getInstance(userRepository,mealRepository,this);

        presenter.init();
    }

    @Override
    public void showMeals(List<Meal>mealList, List<Bitmap>mealImages){
        getActivity().runOnUiThread(() -> {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                    this.getContext(),
                    LinearLayoutManager.VERTICAL,
                    false
            );
            recyclerView.setLayoutManager(linearLayoutManager);
            int itemSpacing = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    16,
                    getResources().getDisplayMetrics()
            );
            recyclerView.addItemDecoration(new ItemSpacingDecoration(itemSpacing));
            recyclerView.setAdapter(new FavouriteAdapter(mealList,mealImages,presenter));
            if (recyclerView.getOnFlingListener() == null) {
                SnapHelper snapHelper = new PagerSnapHelper();
                snapHelper.attachToRecyclerView(recyclerView);
            }
        });

    }

    public void showMealsOnUi(List<Meal>mealList, List<Bitmap>mealImages){

    }
    @Override
    public Object provideFavContext() {
        return this.getContext();
    }


}