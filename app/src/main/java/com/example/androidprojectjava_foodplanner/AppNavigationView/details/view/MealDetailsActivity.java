package com.example.androidprojectjava_foodplanner.AppNavigationView.details.view;

import static android.view.View.INVISIBLE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.window.OnBackInvokedDispatcher;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavAction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.bumptech.glide.Glide;
import com.example.androidprojectjava_foodplanner.AppNavigationView.details.presenter.MealDetailsPresenter;
import com.example.androidprojectjava_foodplanner.AppNavigationView.home.view.HomeAdapter;
import com.example.androidprojectjava_foodplanner.AppNavigationView.home.view.ItemSpacingDecoration;
import com.example.androidprojectjava_foodplanner.AppNavigationView.navigationActivity.view.NavigationActivity;
import com.example.androidprojectjava_foodplanner.R;
import com.example.androidprojectjava_foodplanner.local.database.MealLocalDataSource;
import com.example.androidprojectjava_foodplanner.main.view.LoadActivity;
import com.example.androidprojectjava_foodplanner.main.view.LoadActivityComm;
import com.example.androidprojectjava_foodplanner.main.view.MainActivity;
import com.example.androidprojectjava_foodplanner.model.pojo.Ingredient;
import com.example.androidprojectjava_foodplanner.model.pojo.Meal;
import com.example.androidprojectjava_foodplanner.model.repository.MealRepository;
import com.example.androidprojectjava_foodplanner.model.repository.UserRepository;
import com.example.androidprojectjava_foodplanner.remote.meal.MealRemoteDataSource;
import com.example.androidprojectjava_foodplanner.remote.meal.ingredients.IngredientsRemoteDataSource;
import com.example.androidprojectjava_foodplanner.remote.user.firebase.firebaseAuth.UserAuthentication;
import com.example.androidprojectjava_foodplanner.remote.user.firebase.firebaseDB.UserRemoteDataSource;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class MealDetailsActivity extends AppCompatActivity implements MealDetailsContract{
    private String sender;
    private LoadActivityComm loadActivity;
    private MealDetailsPresenter presenter;
    private FloatingActionButton floatingActionButton;
    private FloatingActionButton scheduleButton;

    private List<Bitmap> ingredientImages;
    private List<Bitmap> mealImage;

    private Meal savedMeal;
    private int _mealID;

    private String mealDate;

    private Handler mainHandler;
    private MaterialDatePicker dateCalendar;
    private TextView instructionsView;
    private TextView titleView;
    private RecyclerView recyclerView;
    private MaterialCardView videoCard;
    private ImageView mealImageView;
    private WebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("MealDetailsActivityLifeCycle","onCreate");
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_meal_details);

        instructionsView = findViewById(R.id.mealDetailsInstructionsTextView);
        titleView = findViewById(R.id.mealTitleTextView);
        recyclerView = findViewById(R.id.mealDetailsIngredientsRecycler);
        videoCard = findViewById(R.id.mealDetailsVideoCard);
        webView = findViewById(R.id.mealDetailsWebView);
        mealImageView = findViewById(R.id.mealDetailsImageView);


        Intent intent = getIntent();
        int mealID = intent.getIntExtra("mealId", 0);
        _mealID = mealID;
        String senderID = intent.getStringExtra("senderID");

        Log.i("MealDetailsActivitySenderId",senderID);


        MealRepository mealRepository = MealRepository.getInstance(
                MealRemoteDataSource.getInstance(this),
                MealLocalDataSource.getInstance(this)
        );

        UserRepository userRepository = UserRepository.getInstance(
                MealLocalDataSource.getInstance(this),
                MealRemoteDataSource.getInstance(this),
                UserAuthentication.getInstance(),
                UserRemoteDataSource.getInstance(this)
        );

        IngredientsRemoteDataSource ingredientsRemoteDataSource = IngredientsRemoteDataSource.getInstance(this);
        presenter = MealDetailsPresenter.getInstance(mealRepository,
                this,
                userRepository,
                loadActivity,
                ingredientsRemoteDataSource);
        floatingActionButton = findViewById(R.id.mealDetailsFavouriteBtn);
        scheduleButton = findViewById(R.id.scheduleButton);

        presenter.init();
        if(!senderID.equals("CalendarFragment")){
            presenter.getMealDetails(mealID,senderID);        }
        else{
            mealDate = intent.getStringExtra("mealDate");
            presenter.getMealDetailsByDate(mealDate);
        }

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onFavClicked(mealID);
            }
        });

        scheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDial();
            }
        });

        mainHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch(msg.what){
                    case 1:
                        Bundle bundle = msg.getData();
                        Meal meal = (Meal) bundle.getSerializable("meal");
                        List<Ingredient> ingredients = (List<Ingredient>) bundle.getSerializable("ingredientList");
                        showMealDetailsMainThread(meal,ingredients);
                        break;
                    case 2:
                        showMealDetailsFromFavMainThread(mealImage,ingredientImages);
                        break;
                }

            }
        };

    }


    @Override
    public void showLoading() {

    }

    public void showMealDetailsFromFav(List<Bitmap>mealImage,List<Bitmap>ingredientImages,Meal meal){
        this.ingredientImages = ingredientImages;
        this.mealImage = mealImage;
        this.savedMeal = meal;
        Message msg = Message.obtain();
        msg.what = 2;
        mainHandler.sendMessage(msg);
    }

    @Override
    public void showSavingPlannedSuccess() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MealDetailsActivity.this.getApplicationContext(), "Meal planned successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void showSavingPlannedFailure() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MealDetailsActivity.this.getApplicationContext(), "Failed to save Meal", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showMealDetailsFromFavMainThread(List<Bitmap>mealImage,List<Bitmap>ingredientImages){
        Meal meal = this.savedMeal;
        Glide.with(this.getApplicationContext()).asBitmap().load(mealImage.get(0)).into(mealImageView);
        instructionsView.setText(meal.getInstructions());
        titleView.setText(meal.getName());


        videoCard.setVisibility(View.VISIBLE);


        String youtubeUrl = meal.getVideoUrl();
        String videoId = extractYouTubeId(youtubeUrl);
        setupYouTubeVideo(videoId);

        List<Ingredient>ingredientList = new ArrayList<>();

        for(int i=0;i<meal.getIngredients().size();i++){
            ingredientList.add(new Ingredient(meal.getIngredients().get(i),meal.getMeasurements().get(i),ingredientImages.get(i)));
        }

        IngredientsAdapter ingredientsAdapter = new IngredientsAdapter(ingredientList);
        recyclerView.setAdapter(ingredientsAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                this.getApplicationContext(),
                LinearLayoutManager.HORIZONTAL,
                false
        );
        int itemSpacing = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                16,
                getResources().getDisplayMetrics()
        );

        recyclerView.addItemDecoration(new ItemSpacingDecoration(itemSpacing));
        recyclerView.setAdapter(new IngredientsAdapter(ingredientList));

        if (recyclerView.getOnFlingListener() == null) {
            SnapHelper snapHelper = new PagerSnapHelper();
            snapHelper.attachToRecyclerView(recyclerView);
        }
    }

    @Override
    public void showMealDetails(Meal meal, List<Ingredient> ingredientList) {
        Message msg = Message.obtain();
        msg.what = 1;
        Bundle bundle = new Bundle();
        bundle.putSerializable("meal", meal);
        bundle.putSerializable("ingredientList", (Serializable) ingredientList);
        msg.setData(bundle);
        mainHandler.sendMessage(msg);
    }

    public void showMealDetailsMainThread(Meal meal,List<Ingredient> ingredientList){
        Glide.with(this.getApplicationContext()).load(meal.getImageUrl()).into(mealImageView);
        instructionsView.setText(meal.getInstructions());
        titleView.setText(meal.getName());


        videoCard.setVisibility(View.VISIBLE);

        String youtubeUrl = meal.getVideoUrl();
        String videoId = extractYouTubeId(youtubeUrl);
        setupYouTubeVideo(videoId);

        Log.i("ShowingDetails","Success");
        IngredientsAdapter ingredientsAdapter = new IngredientsAdapter(ingredientList);
        RecyclerView recyclerView = findViewById(R.id.mealDetailsIngredientsRecycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                this.getApplicationContext(),
                LinearLayoutManager.HORIZONTAL,
                false
        );
        int itemSpacing = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                16,
                getResources().getDisplayMetrics()
        );

        recyclerView.addItemDecoration(new ItemSpacingDecoration(itemSpacing));
        recyclerView.setAdapter(new IngredientsAdapter(ingredientList));

        if (recyclerView.getOnFlingListener() == null) {
            SnapHelper snapHelper = new PagerSnapHelper();
            snapHelper.attachToRecyclerView(recyclerView);
        }
    }

    @Override
    public void favCallBack(FavCallBack status) {

    }

    @Override
    public void failedToFetch() {
        Toast.makeText(this, "Failed to fetch", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void highlightFav() {
        floatingActionButton.setSupportImageTintList(
                ColorStateList.valueOf(
                        ContextCompat.getColor(getApplicationContext(), R.color.primary_pistachio)
                )
        );
    }

    @Override
    public void unhighlightFav() {
        floatingActionButton.setSupportImageTintList(
                ColorStateList.valueOf(
                        ContextCompat.getColor(getApplicationContext(),  R.color.secondary_text)
                )
        );
    }

    private void configureWebView(WebView webView) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
    }

    private void setupYouTubeVideo(String youtubeVideoId) {
        WebView webView = findViewById(R.id.mealDetailsWebView);
        MaterialCardView videoCard = findViewById(R.id.mealDetailsVideoCard);

        if (youtubeVideoId != null && !youtubeVideoId.isEmpty()) {
            videoCard.setVisibility(View.VISIBLE);
            configureWebView(webView);
            loadYouTubeVideo(webView, youtubeVideoId);
        } else {
            videoCard.setVisibility(View.GONE);
        }
    }


    private void loadYouTubeVideo(WebView webView, String videoId) {
        String html = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "  <body>\n" +
                "    <div id=\"player\"></div>\n" +
                "    <script>\n" +
                "      var tag = document.createElement('script');\n" +
                "      tag.src = \"https://www.youtube.com/iframe_api\";\n" +
                "      var firstScriptTag = document.getElementsByTagName('script')[0];\n" +
                "      firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);\n" +
                "\n" +
                "      var player;\n" +
                "      function onYouTubeIframeAPIReady() {\n" +
                "        player = new YT.Player('player', {\n" +
                "          height: '100%',\n" +
                "          width: '100%',\n" +
                "          videoId: '" + videoId + "',\n" +
                "          playerVars: {\n" +
                "            'playsinline': 1,\n" +
                "            'controls': 1,\n" +
                "            'rel': 0\n" +
                "          },\n" +
                "          events: {\n" +
                "            'onReady': onPlayerReady\n" +
                "          }\n" +
                "        });\n" +
                "      }\n" +
                "\n" +
                "      function onPlayerReady(event) {\n" +
                "      }\n" +
                "    </script>\n" +
                "  </body>\n" +
                "</html>";

        webView.loadData(html, "text/html", "utf-8");
    }

    private String extractYouTubeId(String url) {
        String videoId = "";
        if (url != null && url.trim().length() > 0) {
            String[] split = url.split("v=");
            if (split.length > 1) {
                videoId = split[1];
                int ampPos = videoId.indexOf('&');
                if (ampPos != -1) {
                    videoId = videoId.substring(0, ampPos);
                }
            }
        }
        return videoId;
    }

    void showDatePickerDial(){
        long today = MaterialDatePicker.todayInUtcMilliseconds();
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTimeInMillis(today);
        calendar.add(Calendar.DAY_OF_MONTH, 7);
        long oneWeekFromToday = calendar.getTimeInMillis();

        CalendarConstraints.DateValidator dateValidator = new CalendarConstraints.DateValidator() {
            @Override
            public boolean isValid(long date) {
                return date >= today && date <= oneWeekFromToday;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(@NonNull Parcel dest, int flags) {

            }
        };

        CalendarConstraints constraints = new CalendarConstraints.Builder()
                .setValidator(dateValidator)
                .setStart(today)
                .setEnd(oneWeekFromToday)
                .build();


        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Schedule Meal")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setCalendarConstraints(constraints)
                .setTheme(R.style.PistachioDatePickerTheme)
                .build();
        datePicker.show(getSupportFragmentManager(),"DatePicker");

        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(selection);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH) + 1;
                int year = calendar.get(Calendar.YEAR);
                presenter.onScheduleClicked(_mealID,day,month,year);
            }
        });

    }

    @Override
    public void hideFavBtn(){
        floatingActionButton.setVisibility(INVISIBLE);
    }

    @Override
    public void hideScheduleBtn(){
        scheduleButton.setVisibility(INVISIBLE);
    }

    @Override
    public Object provideContext(){
        return this.getApplicationContext();
    }

    @Override
    public void showPlannedMeal(Meal meal,Bitmap imageMeal,List<Bitmap> ingredientImages){

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.i("MealDetailsActivityLifeCycle","onDestroy");
    }

}