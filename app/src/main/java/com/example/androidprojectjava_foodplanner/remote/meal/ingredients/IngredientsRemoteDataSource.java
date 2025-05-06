package com.example.androidprojectjava_foodplanner.remote.meal.ingredients;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.androidprojectjava_foodplanner.AppNavigationView.details.presenter.MealDetailsPresenter;
import com.example.androidprojectjava_foodplanner.model.pojo.Ingredient;
import com.example.androidprojectjava_foodplanner.remote.meal.MealNetworkService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IngredientsRemoteDataSource {
    private IngredientsService ingredientsService;
    private static IngredientsRemoteDataSource instance = null;

    private final static String BASE_URL = "https://www.themealdb.com/images/ingredients/";

    private static Context myContext;
    private IngredientsRemoteDataSource(Object context){
        Context _context = (Context)context;
        this.myContext = _context;
        int cacheSize = 10 * 1024 * 1024;
        Cache cache = new Cache( _context.getCacheDir(), cacheSize);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().cache(cache).build();

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient);
        Retrofit retrofit = retrofitBuilder.build();
        ingredientsService =retrofit.create(IngredientsService.class);
    }

    public static IngredientsRemoteDataSource getInstance(Object context) {
        if (instance == null) {
            instance = new IngredientsRemoteDataSource(context);
        }
        return instance;
    }

    public static String formatIngredientName(String ingredientName){
        return (ingredientName.replace(" ","_") + "-small.png");
    }

    public void getIngredientImage(String ingredientName,IngredientsNetworkCB callBack){
        Call<ResponseBody> call = ingredientsService.getIngredientImage(ingredientName);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                new Thread(){
                    @Override
                    public void run(){
                        InputStream inputStream = response.body().byteStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        if(bitmap == null)callBack.onFailure("Couldn't load image");
                        else callBack.onSuccess(bitmap);
                    }
                }.start();
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callBack.onFailure(t.getMessage());
            }
        });
    }

    public void getIngredientsImages(List<String> ingredients, IngredientsNetworkCB callBack){

        List<Bitmap> bitmaps = new ArrayList<>(Collections.nCopies(ingredients.size(), null));
        AtomicInteger counter = new AtomicInteger(0);

        class CheckAndReturn implements Runnable{
            List<Bitmap> bitmaps;
            public CheckAndReturn(List<Bitmap> bitmaps){
                this.bitmaps = bitmaps;
            }

            public void check(Bitmap bitmap,int index){
                bitmaps.set(index,bitmap);
                this.run();
            }

            @Override
            public void run() {
                if (counter.incrementAndGet() == ingredients.size()) {
                    callBack.onSuccess(bitmaps);
                }
            }
        }

        CheckAndReturn checkAndReturn = new CheckAndReturn(bitmaps);

        for (int i = 0; i < ingredients.size(); i++) {
            final int index = i;
            String formattedName = formatIngredientName(ingredients.get(i));
            getIngredientImage(formattedName, new IngredientsNetworkCB() {
                @Override
                public void onSuccess(List<Bitmap> imagesBitmap) {

                }
                @Override
                public void onSuccess(Bitmap imageBitmap) {
                    checkAndReturn.check(imageBitmap,index);
                }

                @Override
                public void onFailure(String message) {
                    checkAndReturn.check(null,index);
                }
            });
        }

    }




    private static class SaveIngredientsToLocal implements Runnable{
        private List<Ingredient> ingredientList;

        public SaveIngredientsToLocal(List<Ingredient> ingredientList ){
            this.ingredientList = ingredientList;
        }
        @Override
        public void run() {
            File imagesDir = new File(myContext.getFilesDir(), "images");
            if (!imagesDir.exists()) {
                imagesDir.mkdirs();
            }

            for(int i=0;i<ingredientList.size();i++){
                FileOutputStream out = null;
                String fileName = IngredientsRemoteDataSource.formatIngredientName(ingredientList.get(i).getName()) + ".png";
                File imageFile = new File(imagesDir, fileName);
                try {
                    out = new FileOutputStream(imageFile);
                    Bitmap bitmap = ingredientList.get(i).getImage();
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,out);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                finally {
                    try{
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static class SaveIngredients implements IngredientsNetworkCB{
        private List<String> ingredientNames;
        public SaveIngredients(List<String>ingredientNames){
            this.ingredientNames = ingredientNames;

        }

        @Override
        public void onSuccess(List<Bitmap> imagesBitmap) {
            List<Ingredient> ingredientList = new ArrayList<>();
            for(int i=0;i<ingredientNames.size();i++){
                ingredientList.add(new Ingredient(ingredientNames.get(i),"",imagesBitmap.get(i)));
            }
            Thread th = new Thread(new SaveIngredientsToLocal(ingredientList));
            th.start();
        }

        @Override
        public void onSuccess(Bitmap imageBitmap) {

        }

        @Override
        public void onFailure(String message) {

        }
    }

    public void getIngredientImagesFromLocal(List<String> ingredientNames,IngredientsNetworkCB callBack){
        List<Bitmap> bitmaps = new ArrayList<>();
        File imagesDir = new File(myContext.getFilesDir(), "images");
        if(!imagesDir.exists()){
            callBack.onFailure("no images found");
            return;
        }

        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                for (String name : ingredientNames) {
                    String fileName = IngredientsRemoteDataSource.formatIngredientName(name) + ".png";
                    File imageFile = new File(imagesDir, fileName);
                    if (imageFile.exists()) {
                        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                        bitmaps.add(bitmap);
                    } else {
                        bitmaps.add(null);
                    }
                }
                callBack.onSuccess(bitmaps);
            }
        });
        th.start();
    }

    public void deleteAllIngredientsImages(){
        File imagesDir = new File(myContext.getFilesDir(), "images");
        if (imagesDir.exists() && imagesDir.isDirectory()) {
            File[] files = imagesDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    file.delete();
                }
            }
        }
    }
}
