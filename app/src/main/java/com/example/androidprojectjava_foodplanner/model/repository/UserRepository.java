package com.example.androidprojectjava_foodplanner.model.repository;

import android.util.Log;

import androidx.annotation.Nullable;

import com.example.androidprojectjava_foodplanner.local.database.MealLocalDataSource;
import com.example.androidprojectjava_foodplanner.local.database.OperationState;
import com.example.androidprojectjava_foodplanner.model.pojo.FavouriteMeal;
import com.example.androidprojectjava_foodplanner.model.pojo.PlannedMeal;
import com.example.androidprojectjava_foodplanner.model.pojo.PlannerUser;
import com.example.androidprojectjava_foodplanner.remote.meal.MealRemoteDataSource;
import com.example.androidprojectjava_foodplanner.remote.user.firebase.firebaseAuth.OnDeletionCallBack;
import com.example.androidprojectjava_foodplanner.remote.user.firebase.firebaseAuth.OnLoginCallBack;
import com.example.androidprojectjava_foodplanner.remote.user.firebase.firebaseAuth.OnSignupCallBack;
import com.example.androidprojectjava_foodplanner.remote.user.firebase.firebaseAuth.UserAuthentication;
import com.example.androidprojectjava_foodplanner.remote.user.firebase.firebaseDB.AddUserCB;
import com.example.androidprojectjava_foodplanner.remote.user.firebase.firebaseDB.GetUserCB;
import com.example.androidprojectjava_foodplanner.remote.user.firebase.firebaseDB.UserRemoteDataSource;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class UserRepository{
    private static UserRepository instance = null;
    private static FirebaseUser firebaseUser;
    private MealRepository mealRepository;
    private UserAuthentication firebaseAuth;
    private UserRemoteDataSource firebaseDB;
    private MealLocalDataSource mealLocalDataSource;

    private UserRepository(MealLocalDataSource mealLocalDataSource,
                           UserAuthentication firebaseAuth,
                           UserRemoteDataSource firebaseDB){
        mealRepository = MealRepository.getInstance(null,mealLocalDataSource);
        this.firebaseAuth = firebaseAuth;
        this.firebaseDB = firebaseDB;
        this.mealLocalDataSource = mealLocalDataSource;
    }

    public static UserRepository getInstance(MealLocalDataSource mealLocalDataSource,
                                             UserAuthentication firebaseAuth,
                                             UserRemoteDataSource firebaseDB)
    {
        if(instance == null){
            instance = new UserRepository(mealLocalDataSource,firebaseAuth,firebaseDB);
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

    public void signup(String email, String password,String name,OnSignupCallBack CB){
        firebaseAuth.signUp(email,password,name,CB);
    }

    public void login(String email, String password, OnLoginCallBack CB){
        firebaseAuth.login(email,password,CB);
    }

    public void logout(){
        firebaseAuth.logOut();
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

    public void addPlannedMealToRemote(PlannedMeal plannedMeal,AddUserCB callBack){
        firebaseDB.addUserPlannedMeal(plannedMeal,firebaseUser.getUid(),callBack);
    }

    public void addFavouriteMealToRemote(FavouriteMeal favouriteMeal,AddUserCB callBack){
        firebaseDB.addUserFavouriteMeal(favouriteMeal,firebaseUser.getUid(),callBack);
    }

    public void removeFavouriteMealFromRemote(int mealID,AddUserCB callBack){
        firebaseDB.removeFavouriteMeal(firebaseUser.getUid(),mealID,callBack);
    }

    public void removePlannedMealFromRemote(int mealID,AddUserCB callBack){
        firebaseDB.removePlannedMeal(firebaseUser.getUid(),mealID,callBack);
    }

    public void removePlannedMealByDateFromRemote(String date,AddUserCB callBack){
        firebaseDB.removePlannedMealByDate(firebaseUser.getUid(),date,callBack);
    }
    public void changeUserDisplayName(String uid,String new_name,@Nullable AddUserCB callBack){
        firebaseDB.changeUserDisplayName(uid,new_name,callBack);
    }

}
