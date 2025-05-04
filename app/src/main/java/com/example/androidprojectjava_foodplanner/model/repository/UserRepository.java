package com.example.androidprojectjava_foodplanner.model.repository;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.androidprojectjava_foodplanner.local.database.MealLocalDataSource;
import com.example.androidprojectjava_foodplanner.local.database.OperationState;
import com.example.androidprojectjava_foodplanner.model.pojo.FavouriteMeal;
import com.example.androidprojectjava_foodplanner.model.pojo.Meal;
import com.example.androidprojectjava_foodplanner.model.pojo.PlannedMeal;
import com.example.androidprojectjava_foodplanner.model.pojo.PlannerUser;
import com.example.androidprojectjava_foodplanner.remote.meal.MealRemoteDataSource;
import com.example.androidprojectjava_foodplanner.remote.meal.ingredients.IngredientsRemoteDataSource;
import com.example.androidprojectjava_foodplanner.remote.user.firebase.firebaseAuth.OnDeletionCallBack;
import com.example.androidprojectjava_foodplanner.remote.user.firebase.firebaseAuth.OnLoginCallBack;
import com.example.androidprojectjava_foodplanner.remote.user.firebase.firebaseAuth.OnSignupCallBack;
import com.example.androidprojectjava_foodplanner.remote.user.firebase.firebaseAuth.UserAuthentication;
import com.example.androidprojectjava_foodplanner.remote.user.firebase.firebaseDB.AddUserCB;
import com.example.androidprojectjava_foodplanner.remote.user.firebase.firebaseDB.GetUserCB;
import com.example.androidprojectjava_foodplanner.remote.user.firebase.firebaseDB.UserRemoteDataSource;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class UserRepository{
    private static UserRepository instance = null;
    private static FirebaseUser firebaseUser;
    private MealRepository mealRepository;
    private UserAuthentication firebaseAuth;
    private UserRemoteDataSource firebaseDB;
    private MealLocalDataSource mealLocalDataSource;

    private UserRepository(MealLocalDataSource mealLocalDataSource,
                           MealRemoteDataSource mealRemoteDataSource,
                           UserAuthentication firebaseAuth,
                           UserRemoteDataSource firebaseDB){
        mealRepository = MealRepository.getInstance(mealRemoteDataSource,mealLocalDataSource);
        this.firebaseAuth = firebaseAuth;
        this.firebaseDB = firebaseDB;
        this.mealLocalDataSource = mealLocalDataSource;
    }

    public static UserRepository getInstance(MealLocalDataSource mealLocalDataSource,
                                             MealRemoteDataSource mealRemoteDataSource,
                                             UserAuthentication firebaseAuth,
                                             UserRemoteDataSource firebaseDB)
    {
        if(instance == null){
            instance = new UserRepository(mealLocalDataSource,mealRemoteDataSource,firebaseAuth,firebaseDB);
        }
        firebaseUser = firebaseAuth.getCurrentUser();
        return instance;
    }

    public void setFirebaseUser(FirebaseUser firebaseUser){
        UserRepository.firebaseUser = firebaseUser;
    }

    public boolean signedIn(){
        return firebaseAuth.getCurrentUser() != null;
    }

    public void signup(String email, String password,String name,OperationCB CB){

        AddUserCB addUserCB = new AddUserCB() {
            @Override
            public void onSuccess() {
                syncRemoteToLocal(new SyncingCallBacks() {
                    @Override
                    public void taskCompleted(int status) {
                        if(status == SUCCESS){
                            CB.onSuccess();
                        }
                        else{
                            CB.onFailure(OperationCB.NETWORK_ERROR);
                        }
                    }
                });

            }

            @Override
            public void onFailure() {
                CB.onFailure(OperationCB.NETWORK_ERROR);
            }
        };

        OperationState operationState = new OperationState() {
            @Override
            public void onSuccess() {
                if(firebaseAuth.getCurrentUser() != null) {
                    PlannerUser plannerUser = new PlannerUser();
                    plannerUser.setEmail(email);
                    plannerUser.setName(name);
                    plannerUser.setId(firebaseAuth.getCurrentUser().getUid());
                    firebaseDB.addOrChangeUserDataById(plannerUser, addUserCB);
                }
                else{
                    CB.onFailure(OperationCB.NETWORK_ERROR);
                }
            }

            @Override
            public void onFailure() {

            }
        };

        firebaseAuth.signUp(email, password, name, new OnSignupCallBack() {
            @Override
            public void onSuccess(FirebaseUser user) {
                mealLocalDataSource.deleteAllLocalMeals(operationState);
            }

            @Override
            public void onFailure(int errorID) {
                CB.onFailure(errorID);
            }
        });
    }

    public void login(String email, String password, OperationCB CB){
        OperationState operationState = new OperationState() {
            @Override
            public void onSuccess() {
                syncLocalToRemote(new SyncingCallBacks() {
                    @Override
                    public void taskCompleted(int status) {
                        if(status == SUCCESS){
                            CB.onSuccess();
                        }
                        else{
                            CB.onFailure(OperationCB.NETWORK_ERROR);
                        }
                    }
                });
            }

            @Override
            public void onFailure() {

            }
        };


        firebaseAuth.login(email, password, new OnLoginCallBack() {
            @Override
            public void onSuccess(FirebaseUser user) {
                mealLocalDataSource.deleteAllLocalMeals(operationState);
            }

            @Override
            public void onFailure(int errorID) {
                CB.onFailure(errorID);
            }
        });
    }

    public void logout(){
        firebaseAuth.logOut();
        mealRepository.deleteAllLocalMeals(null);
    }

    public void deleteCurrentUser(OnDeletionCallBack callBack){
        firebaseAuth.deleteCurrentUser(callBack);
        mealRepository.deleteAllLocalMeals(null);
    }

    public void syncLocalToRemote(SyncingCallBacks CB){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user == null){
            CB.taskCompleted(SyncingCallBacks.NOT_LOGGED_IN);
            return;
        }
        final PlannerUser[] myUser = {null};
        /*
        * 0 -> fetching
        * 1 -> succeeded in fetching
        * 2 -> failed in fetching
        * */
        final int[] fetching = {0};
        final boolean[] insertedArr = {false,false};



        Runnable notifyuserRunnable = new Runnable() {
            @Override
            public void run() {
                if(insertedArr[0] && insertedArr[1]){
                    Log.i("syncLocalToRemote","inserted both");
                    CB.taskCompleted(SyncingCallBacks.SUCCESS);
                }
            }
        };

        Runnable insertToDataBaseRunnable = new Runnable() {
            @Override
            public void run() {
                    mealRepository.deleteAllLocalMeals(new OperationState() {
                        @Override
                        public void onSuccess() {
                            mealRepository.insertPlannedMealsLocal(myUser[0].getPlannedMeals(), new OperationState() {
                                @Override
                                public void onSuccess() {

                                    Log.i("syncLocalToRemote","inserted planned");
                                    insertedArr[0] = true;
                                    notifyuserRunnable.run();
                                }

                                @Override
                                public void onFailure() {
                                    Log.i("syncLocalToRemote","Failed to insert planned");
                                }
                            });

                            mealRepository.insertFavouriteMealsLocal(myUser[0].getFavouriteMeals(), new OperationState() {
                                @Override
                                public void onSuccess() {
                                    Log.i("syncLocalToRemote","inserted favourite");
                                    insertedArr[1] = true;
                                    notifyuserRunnable.run();
                                }

                                @Override
                                public void onFailure() {
                                    Log.i("syncLocalToRemote","Failed to insert favourite");
                                }
                            });

                        }

                        @Override
                        public void onFailure() {

                        }
                    });
            }
        };

        firebaseDB.getUserDataById(user.getUid(), new GetUserCB() {
            @Override
            public void onSuccess(PlannerUser plannerUser) {
                myUser[0] = plannerUser;
                Log.i("syncLocalToRemote","fetched user with favourite meal: " + plannerUser.getFavouriteMeals().size() +
                        " and planned meals: "
                        + plannerUser.getPlannedMeals().size());
                insertToDataBaseRunnable.run();
            }

            @Override
            public void onFailure() {
                fetching[0] = 2;
                CB.taskCompleted(SyncingCallBacks.NETWORK_ERROR);
            }
        });

    }

    public void syncMealImages(Object context){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user == null){
            Log.i("SyncingMealImages","User is null");
            return;
        }
        Log.i("SyncingMealImages","User is not null");
        firebaseDB.getUserDataById(user.getUid(), new GetUserCB() {
            @Override
            public void onSuccess(PlannerUser plannerUser) {
                List<PlannedMeal> plannedMeals = plannerUser.getPlannedMeals();
                List<FavouriteMeal> favouriteMeals = plannerUser.getFavouriteMeals();
                Log.i("SyncingMealImages","Succcess");

                List<Meal> _plannedMeals = new ArrayList<>();
                List<Meal> _favouriteMeals = new ArrayList<>();

                for(PlannedMeal plannedMeal: plannedMeals){
                    _plannedMeals.add(plannedMeal.getMeal());
                }
                _favouriteMeals.addAll(favouriteMeals);

                IngredientsRemoteDataSource ingredientsRemoteDataSource = IngredientsRemoteDataSource.getInstance(context);

                for(Meal meal: _plannedMeals){
                    List<String>ingredientsNames = new ArrayList<>();
                    for(int i=0;i<meal.getIngredients().size();i++){
                        ingredientsNames.add(meal.getIngredients().get(i));
                    }
                    ingredientsRemoteDataSource.getIngredientsImages(
                            ingredientsNames,
                            new IngredientsRemoteDataSource.SaveIngredients(ingredientsNames)
                    );
                    Thread myThread = new Thread(new SaveMealImageToLocal(meal,context));
                    myThread.start();
                }

                for(Meal meal: _favouriteMeals){
                    List<String>ingredientsNames = new ArrayList<>();
                    for(int i=0;i<meal.getIngredients().size();i++){
                        ingredientsNames.add(meal.getIngredients().get(i));
                    }
                    ingredientsRemoteDataSource.getIngredientsImages(
                            ingredientsNames,
                            new IngredientsRemoteDataSource.SaveIngredients(ingredientsNames)
                    );
                    Thread myThread = new Thread(new SaveMealImageToLocal(meal,context));
                    myThread.start();
                }
            }
            @Override
            public void onFailure() {
                Log.i("SyncingMealImages","failure");
            }
        });
    }

    public void syncRemoteToLocal(SyncingCallBacks CB){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user == null){
            CB.taskCompleted(SyncingCallBacks.NOT_LOGGED_IN);
            Log.i("syncRemoteToLocal","no user");
        }
        else{
            PlannerUser myPlannerUser = new PlannerUser();
            myPlannerUser.setId(user.getUid());
            myPlannerUser.setName(user.getDisplayName());
            myPlannerUser.setEmail(user.getEmail());
            final boolean[] fetchedArr = {false,false};

            Log.i("syncRemoteToLocal","got user");

            Runnable uploadRunnable = new Runnable() {
                @Override
                public void run() {
                    if(fetchedArr[0] && fetchedArr[1]){
                        firebaseDB.addOrChangeUserDataById(myPlannerUser, new AddUserCB() {
                            @Override
                            public void onSuccess() {
                                CB.taskCompleted(SyncingCallBacks.SUCCESS);
                                Log.i("syncRemoteToLocal","upload success");
                            }

                            @Override
                            public void onFailure() {
                                CB.taskCompleted(SyncingCallBacks.NETWORK_ERROR);
                                Log.i("syncRemoteToLocal","upload failed");
                            }
                        });
                    }
                }
            };

            mealRepository.getStoredFavouriteMeals(new FavouriteMealsDBCallBack() {
                @Override
                public void onTaskCompleted(List<FavouriteMeal> meals) {
                    Log.i("syncRemoteToLocal","fetched favourite");
                    myPlannerUser.setFavouriteMeals(meals);
                    fetchedArr[0] = true;
                    uploadRunnable.run();
                }
            });

            mealRepository.getStoredPlannedMeals(new PlannedMealsDBCallBack() {
                @Override
                public void onTaskCompleted(List<PlannedMeal> meals) {
                    Log.i("syncRemoteToLocal","fetched planned");
                    myPlannerUser.setPlannedMeals(meals);
                    fetchedArr[1] = true;
                    uploadRunnable.run();
                }
            });
        }
    }

    public void addFavouriteMeal(FavouriteMeal meal,OperationCB operationState){
        if(firebaseAuth.getCurrentUser() == null){
            operationState.onFailure(OperationCB.USER_NOT_LOGGED_IN);
            return;
        }
        mealLocalDataSource.insertFavouriteMeal(meal, new OperationState() {
            @Override
            public void onSuccess() {
                firebaseDB.addUserFavouriteMeal(meal, firebaseAuth.getCurrentUser().getUid(), new AddUserCB() {
                    @Override
                    public void onSuccess() {
                        operationState.onSuccess();
                    }

                    @Override
                    public void onFailure() {
                        mealLocalDataSource.deleteFavouriteMeal(meal,null);
                        operationState.onFailure(OperationCB.NETWORK_ERROR);
                    }
                });
            }

            @Override
            public void onFailure() {
                operationState.onFailure(OperationCB.DUPLICTE_FAVOURITE_MEAL);
            }
        });
    }

    public void removeFavouriteMeal(FavouriteMeal meal,OperationCB operationState){
        if(firebaseAuth.getCurrentUser() == null){
            operationState.onFailure(OperationCB.USER_NOT_LOGGED_IN);
            return;
        }

        firebaseDB.removeFavouriteMeal(firebaseAuth.getCurrentUser().getUid(), meal.getId(), new AddUserCB() {
            @Override
            public void onSuccess() {
                mealLocalDataSource.deleteFavouriteMeal(meal, new OperationState() {
                    @Override
                    public void onSuccess() {
                        operationState.onSuccess();
                    }

                    @Override
                    public void onFailure() {
                        operationState.onSuccess();
                    }
                });
            }

            @Override
            public void onFailure() {
                operationState.onFailure(OperationCB.NETWORK_ERROR);
            }
        });
    }

    public void addPlannedMeal(PlannedMeal meal, OperationCB operationState){
        if(firebaseAuth.getCurrentUser() == null){
            operationState.onFailure(OperationCB.USER_NOT_LOGGED_IN);
            return;
        }

        mealLocalDataSource.insertPlannedMeal(meal, new OperationState() {
            @Override
            public void onSuccess() {
                firebaseDB.addUserPlannedMeal(meal, firebaseAuth.getCurrentUser().getUid(), new AddUserCB() {
                    @Override
                    public void onSuccess() {
                        operationState.onSuccess();
                    }

                    @Override
                    public void onFailure() {
                        operationState.onFailure(OperationCB.NETWORK_ERROR);
                    }
                });
            }

            @Override
            public void onFailure() {
            operationState.onFailure(OperationCB.DUPLICATE_PLANNED_MEAL);
            }
        });

    }

    public void removePlannedMeal(PlannedMeal meal, OperationCB operationState){
        if(firebaseAuth.getCurrentUser() == null){
            operationState.onFailure(OperationCB.USER_NOT_LOGGED_IN);
            return;
        }
        firebaseDB.removePlannedMeal(firebaseAuth.getCurrentUser().getUid(), meal, new AddUserCB() {
            @Override
            public void onSuccess() {
                mealLocalDataSource.deletePlannedMeal(meal, new OperationState() {
                    @Override
                    public void onSuccess() {
                        operationState.onSuccess();
                    }
                    @Override
                    public void onFailure() {
                        operationState.onSuccess();
                    }
                });
            }
            @Override
            public void onFailure() {
                operationState.onFailure(OperationCB.NETWORK_ERROR);
            }
        });
    }

    public void removePlannedMealByDate(int day,int month,int year,OperationCB operationCB){
        if(firebaseAuth.getCurrentUser() == null){
            operationCB.onFailure(OperationCB.USER_NOT_LOGGED_IN);
            return;
        }
        firebaseDB.removePlannedMealByDate(firebaseAuth.getCurrentUser().getUid(), PlannedMeal.getFormattedDate(day, month, year), new AddUserCB() {
            @Override
            public void onSuccess() {
                mealLocalDataSource.deletePlannedMealByDate(day, month, year, new OperationState() {
                    @Override
                    public void onSuccess() {
                        operationCB.onSuccess();
                    }

                    @Override
                    public void onFailure() {
                        operationCB.onSuccess();
                    }
                });
            }

            @Override
            public void onFailure() {
                operationCB.onFailure(OperationCB.NETWORK_ERROR);
            }
        });
    }

    public void changeUserDisplayName(String uid,String new_name,@Nullable AddUserCB callBack){
        firebaseDB.changeUserDisplayName(uid,new_name,callBack);
    }

    public boolean isLoggedIn(){
        return firebaseAuth.getCurrentUser() != null;
    }

    public String getUserDisplayName(){
        if(firebaseAuth.getCurrentUser() == null){
            return null;
        }
        else if(firebaseAuth.getCurrentUser().getDisplayName() == null){
            return  "Default";
        }
        else{
            return firebaseAuth.getCurrentUser().getDisplayName();
        }
    }

    public static class SaveMealImageToLocal implements Runnable{

        Meal meal;
        private Context context;


        public SaveMealImageToLocal(Meal meal, Object context){
            this.meal = meal;
            this.context = (Context) context;

        }

        public  String formatName(String name){
            return name.replace(" ","_") + ".png";
        }

        @Override
        public void run() {
            File imagesDir = new File(context.getFilesDir(), "mealimages");
            if (!imagesDir.exists()) {
                imagesDir.mkdirs();
            }

            String imageUrl = meal.getImageUrl();
            Log.i("SyncingMealImages",imageUrl);
            if (imageUrl == null || imageUrl.isEmpty()) {
                return;
            }
            String fileName = formatName(meal.getName());
            File imageFile = new File(imagesDir, fileName);
            if (imageFile.exists()) {
                return;
            }
            try {
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(input);
                FileOutputStream out = new FileOutputStream(imageFile);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.close();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static interface MealImagesCB{
        public void onSuccess(List<Bitmap> imagesBitmap);
        public void onFailure(String message);
    }

    public void getMealSavedImages(List<Meal> mealList,Object context,MealImagesCB callBack){
        List<Bitmap> bitmaps = new ArrayList<>();
        Context myContext = (Context) context;
        File imagesDir = new File(myContext.getFilesDir(), "mealimages");
        if (!imagesDir.exists()) {
            callBack.onFailure("no images");
            return;
        }
        for(Meal meal: mealList){
            String fileName = meal.getName().replace(" ","_") + ".png";
            File imageFile = new File(imagesDir, fileName);
            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            bitmaps.add(bitmap);
        }
        callBack.onSuccess(bitmaps);
    }
}
