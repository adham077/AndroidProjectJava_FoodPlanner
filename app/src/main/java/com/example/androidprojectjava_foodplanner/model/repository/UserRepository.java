package com.example.androidprojectjava_foodplanner.model.repository;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.credentials.exceptions.domerrors.NetworkError;

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

    public void signup(String email, String password,String name,OperationCB CB){

        AddUserCB addUserCB = new AddUserCB() {
            @Override
            public void onSuccess() {
                syncRemoteToLocal(null);
                CB.onSuccess();
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

}
