package com.example.androidprojectjava_foodplanner.AppNavigationView.details.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.List;

public class MealDetailsActivity extends AppCompatActivity implements MealDetailsContract{
    private String sender;
    private LoadActivityComm loadActivity;
    private MealDetailsPresenter presenter;
    private FloatingActionButton floatingActionButton;
    private Handler mainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_meal_details);
        Intent intent = getIntent();
        int mealID = intent.getIntExtra("mealId", 0);
        String senderID = intent.getStringExtra("senderID");
        loadActivity = new LoadActivity();
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
        presenter.getMealDetails(mealID,senderID);

        floatingActionButton = findViewById(R.id.mealDetailsFavouriteBtn);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onFavClicked(mealID);
            }
        });

        mainHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                Bundle bundle = msg.getData();
                Meal meal = (Meal) bundle.getSerializable("meal");
                List<Ingredient> ingredients = (List<Ingredient>) bundle.getSerializable("ingredientList");
                showMealDetailsMainThread(meal,ingredients);
            }
        };

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Log.i("MealDetailsActivityBack","Back pressed");
                finish();
            }
        });
    }

    @Override
    public void showLoading() {

    }
    @Override
    public void showMealDetails(Meal meal, List<Ingredient> ingredientList) {
        Message msg = Message.obtain();
        Bundle bundle = new Bundle();
        bundle.putSerializable("meal", meal);
        bundle.putSerializable("ingredientList", (Serializable) ingredientList);
        msg.setData(bundle);
        mainHandler.sendMessage(msg);
    }

    public void showMealDetailsMainThread(Meal meal,List<Ingredient> ingredientList){
        Log.i("MealDetailsActivityShowView","showingMealDetails");
        if(this.getApplicationContext() == null) Log.i("MealDetailsActivityShowView","Null");
        else Log.i("MealDetailsActivityShowView","Not null");
        Glide.with(this.getApplicationContext()).load(meal.getImageUrl()).into((ImageView) findViewById(R.id.mealDetailsImageView));
        ((TextView)findViewById(R.id.mealDetailsInstructionsTextView)).setText(meal.getInstructions());
        ((TextView)findViewById(R.id.mealTitleTextView)).setText(meal.getName());

        WebView webView = findViewById(R.id.mealDetailsWebView);
        MaterialCardView videoCard = findViewById(R.id.mealDetailsVideoCard);
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
                        ContextCompat.getColor(getApplicationContext(), R.color.red)
                )
        );
    }

    @Override
    public void unhighlightFav() {
        floatingActionButton.setSupportImageTintList(
                ColorStateList.valueOf(
                        ContextCompat.getColor(getApplicationContext(),  R.color.primary_pistachio)
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

    @Override
    public Object provideContext(){
        return this.getApplicationContext();
    }
}