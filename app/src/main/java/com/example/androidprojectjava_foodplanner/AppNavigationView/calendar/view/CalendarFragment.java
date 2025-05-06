package com.example.androidprojectjava_foodplanner.AppNavigationView.calendar.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.androidprojectjava_foodplanner.AppNavigationView.calendar.presenter.CalendarPresenter;
import com.example.androidprojectjava_foodplanner.AppNavigationView.details.view.MealDetailsActivity;
import com.example.androidprojectjava_foodplanner.R;
import com.example.androidprojectjava_foodplanner.local.database.MealLocalDataSource;
import com.example.androidprojectjava_foodplanner.model.pojo.Meal;
import com.example.androidprojectjava_foodplanner.model.pojo.PlannedMeal;
import com.example.androidprojectjava_foodplanner.model.repository.MealRepository;
import com.example.androidprojectjava_foodplanner.model.repository.UserRepository;
import com.example.androidprojectjava_foodplanner.remote.meal.MealRemoteDataSource;
import com.example.androidprojectjava_foodplanner.remote.user.firebase.firebaseAuth.UserAuthentication;
import com.example.androidprojectjava_foodplanner.remote.user.firebase.firebaseDB.UserRemoteDataSource;
import com.google.firebase.firestore.auth.User;

import java.util.Calendar;


public class CalendarFragment extends Fragment implements CalendarContract{

    private CalendarView calendarView;
    private TextView selectedDateText;
    private ImageButton removeButton;
    private ImageView mealImage;
    private TextView mealName;
    private TextView emptyText;
    private int mealDay;
    private int mealMonth;
    private int mealYear;
    private CardView cardView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        calendarView = view.findViewById(R.id.calendarView);
        selectedDateText = view.findViewById(R.id.selectedDateText);
        removeButton = view.findViewById(R.id.removeButton);
        mealImage = view.findViewById(R.id.mealImage);
        mealName = view.findViewById(R.id.mealName);
        emptyText = view.findViewById(R.id.emptyText);
        cardView = view.findViewById(R.id.calendarCard);

        MealLocalDataSource mealLocalDataSource = MealLocalDataSource.getInstance(this.getContext());
        MealRemoteDataSource mealRemoteDataSource = MealRemoteDataSource.getInstance(this.getContext());
        UserAuthentication userAuthentication = UserAuthentication.getInstance();
        UserRemoteDataSource userRemoteDataSource = UserRemoteDataSource.getInstance(this.getContext());
        UserRepository userRepository = UserRepository.getInstance(mealLocalDataSource,mealRemoteDataSource,userAuthentication,userRemoteDataSource);
        MealRepository mealRepository = MealRepository.getInstance(mealRemoteDataSource,mealLocalDataSource);
        CalendarPresenter calendarPresenter = CalendarPresenter.getInstance(userRepository,mealRepository,this);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                calendarPresenter.onDateChanged(dayOfMonth,month + 1,year);
                Log.i("CalendarFragmentDate","Date Changed" + dayOfMonth + "/" + month + "/" + year);
            }
        });

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarPresenter.deletePlannedMeal(CalendarFragment.this.mealDay,CalendarFragment.this.mealMonth,CalendarFragment.this.mealYear);
            }
        });

        mealImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MealDetailsActivity.class);
                intent.putExtra("senderID","CalendarFragment");
                intent.putExtra("mealDate", PlannedMeal.getFormattedDate(mealDay,mealMonth,mealYear));
                startActivity(intent);
            }
        });

    }

    @Override
    public void showMealView(Meal meal, Bitmap imageBitmap) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Glide.with(CalendarFragment.this.getContext()).asBitmap().load(imageBitmap).into(mealImage);
                Log.i("ShowMealViewName",meal.getName());
                selectedDateText.setText(
                        ""
                );
                mealName.setText(meal.getName());
                emptyText.setVisibility(View.GONE);
                mealImage.setVisibility(View.VISIBLE);
                mealName.setVisibility(View.VISIBLE);
                removeButton.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public void showNoMealView(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                emptyText.setVisibility(View.VISIBLE);
                mealImage.setVisibility(View.GONE);
                mealName.setVisibility(View.GONE);
                removeButton.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void setMealDate(int day, int month, int year) {
        this.mealDay = day;
        this.mealMonth = month;
        this.mealYear = year;
        selectedDateText.setText(day + "/" + month + "/" + year);
    }

    @Override
    public void failedOp(String message) {
        Toast.makeText(this.getContext(),message,Toast.LENGTH_LONG).show();
    }

    @Override
    public Object provideContext() {
        return this.getContext();
    }

    public void showMealView(){

    }
}