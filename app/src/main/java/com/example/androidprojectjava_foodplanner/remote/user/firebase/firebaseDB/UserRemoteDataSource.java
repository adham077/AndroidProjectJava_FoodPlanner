package com.example.androidprojectjava_foodplanner.remote.user.firebase.firebaseDB;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.androidprojectjava_foodplanner.model.pojo.FavouriteMeal;
import com.example.androidprojectjava_foodplanner.model.pojo.PlannedMeal;
import com.example.androidprojectjava_foodplanner.model.pojo.PlannerUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserRemoteDataSource {
    private static UserRemoteDataSource instance = null;
    private FirebaseFirestore firebaseFirestoreDB;

    private static final String COLLECTION_NAME = "users";
    public UserRemoteDataSource(Object context){
        firebaseFirestoreDB = FirebaseFirestore.getInstance();
    }

    public static UserRemoteDataSource getInstance(Object context) {
        if (instance == null) {
            instance = new UserRemoteDataSource(context);
        }
        return instance;
    }


    public void addUserPlannedMeal(PlannedMeal meal,@NonNull String userId ,@Nullable AddUserCB callBack){
        DocumentReference documentReference = firebaseFirestoreDB.collection(COLLECTION_NAME).document(userId);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isComplete()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()){
                        List<Map<String, Object>> meals = (List<Map<String, Object>>) documentSnapshot.get("plannedMeals");
                        for(Map<String, Object> planned_meal : meals){
                            if(planned_meal.get("date").equals(meal.getDate())){
                                if(callBack!=null)callBack.onSuccess();
                                return;
                            }
                        }
                        firebaseFirestoreDB.collection(COLLECTION_NAME)
                                .document(userId)
                                .update("plannedMeals", FieldValue.arrayUnion(meal))
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isComplete()){
                                            if(callBack!=null)callBack.onSuccess();
                                        }
                                        else{
                                            if(callBack!=null)callBack.onFailure();
                                        }
                                    }
                                });
                    }
                    else{
                        if(callBack!=null)callBack.onFailure();
                    }
                }
                else {
                   if(callBack!=null)callBack.onFailure();
                }
            }
        });
    }

    public void addUserFavouriteMeal(FavouriteMeal meal, @NonNull String userId , @Nullable AddUserCB callBack){

        DocumentReference documentReference = firebaseFirestoreDB.collection(COLLECTION_NAME).document(userId);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()) {
                        List<Map<String, Object>> meals = (List<Map<String, Object>>) documentSnapshot.get("favouriteMeals");
                        for(Map<String, Object> fav_meal : meals){
                            Number listMealId = (Number) fav_meal.get("id");
                            if(listMealId.intValue() == meal.getId()){
                                callBack.onSuccess();
                                return;
                            }
                        }
                        firebaseFirestoreDB.collection(COLLECTION_NAME)
                                .document(userId)
                                .update("favouriteMeals", FieldValue.arrayUnion(meal))
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isComplete()){
                                            if(callBack!=null)callBack.onSuccess();
                                        }
                                        else{
                                            if(callBack!=null)callBack.onFailure();
                                        }
                                    }
                                });
                    }
                    else{
                        callBack.onFailure();
                    }
                }
                else{
                    if(callBack != null)callBack.onFailure();
                }
            }
        });
    }

    public void removeFavouriteMeal(@NonNull String userId,@NonNull int mealID,@Nullable AddUserCB callBack){
        DocumentReference documentReference = firebaseFirestoreDB.collection(COLLECTION_NAME).document(userId);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isComplete()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()){
                        List<Map<String,Object>> meals = (List<Map<String,Object>>)documentSnapshot.get("favouriteMeals");
                        if(meals != null){
                            List<Map<String,Object>> updatedMeals = new ArrayList<>();
                            for(Map<String, Object> meal : meals){
                                Number listMealId = (Number) meal.get("id");
                                if(listMealId != null && listMealId.intValue() != mealID){
                                    updatedMeals.add(meal);

                                }
                            }
                            documentReference.update("favouriteMeals",updatedMeals).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isComplete()){
                                        if(callBack!=null)callBack.onSuccess();
                                    }
                                    else{
                                        if(callBack!=null)callBack.onFailure();
                                    }
                                }
                            });
                        }
                        else{
                            if(callBack!=null)callBack.onSuccess();
                        }
                    }
                    else{
                        if(callBack!=null)callBack.onFailure();
                    }
                }
                else{
                    if(callBack!=null)callBack.onFailure();
                }
            }
        });
    }

    public void removePlannedMeal(@NonNull String userId,@NonNull PlannedMeal myPlannedMeal,@Nullable AddUserCB callBack){
        DocumentReference documentReference = firebaseFirestoreDB.collection(COLLECTION_NAME).document(userId);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()){
                        List<Map<String,Object>> plannedMeals = (List<Map<String,Object>>)documentSnapshot.get("plannedMeals");
                        if(plannedMeals!=null){
                            List<Map<String,Object>> updatedMeals = new ArrayList<>();
                            for(Map<String, Object> plannedMeal : plannedMeals){
                                Map<String,Object> meal = (Map<String, Object>) plannedMeal.get("meal");
                                String date = (String) plannedMeal.get("date");
                                if(date != null && !date.equals(myPlannedMeal.getDate())){
                                    updatedMeals.add(plannedMeal);
                                }
                            }
                            documentReference.update("plannedMeals",updatedMeals).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        if(callBack!=null)callBack.onSuccess();
                                    }
                                    else{
                                        if(callBack!=null)callBack.onFailure();
                                    }
                                }
                            });

                        }
                        else{
                            if(callBack!=null)callBack.onSuccess();
                        }

                    }
                    else{
                        if(callBack!=null)callBack.onFailure();
                    }
                }
                else{
                    if(callBack!=null)callBack.onFailure();
                }
            }
        });
    }

    public void removePlannedMealByDate(@NonNull String userId,@NonNull String date,@Nullable AddUserCB callBack){
        DocumentReference documentReference = firebaseFirestoreDB.collection(COLLECTION_NAME).document(userId);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()){
                        List<Map<String,Object>> plannedMeals = (List<Map<String,Object>>)documentSnapshot.get("plannedMeals");
                        if(plannedMeals != null) {
                            List<Map<String,Object>> updatedMeals = new ArrayList<>();
                            for (Map<String, Object> meal : plannedMeals) {
                                if(!date.equals(meal.get("date"))){
                                    updatedMeals.add(meal);
                                }
                            }
                            documentReference.update("plannedMeals",updatedMeals).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        if(callBack!=null)callBack.onSuccess();
                                    }
                                    else{
                                        if(callBack!=null)callBack.onFailure();
                                    }
                                }
                            });
                        }
                        else{
                            if(callBack!=null)callBack.onSuccess();
                        }

                    }
                    else{
                        if(callBack!=null)callBack.onFailure();
                    }
                }
                else{
                    if(callBack!=null)callBack.onFailure();
                }
            }
        });
    }

    public void addOrChangeUserDataById(PlannerUser plannerUser, @Nullable AddUserCB callBack){
        firebaseFirestoreDB.collection(COLLECTION_NAME)
                .document(plannerUser.getId())
                .set(plannerUser)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            if(callBack!=null)callBack.onSuccess();
                        }
                        else{
                            if(callBack!=null)callBack.onFailure();
                        }
                    }
                });
    }

    public void getUserDataById(String id,GetUserCB callBack){
        firebaseFirestoreDB.collection(COLLECTION_NAME)
                .document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if(documentSnapshot != null){
                                callBack.onSuccess(documentSnapshot.toObject(PlannerUser.class));
                            }
                            else{
                                callBack.onFailure();
                            }
                        }
                        else{
                            callBack.onFailure();
                        }
                    }
                });
    }

    public void changeUserDisplayName(String uid,String new_name,@Nullable AddUserCB callBack){
        firebaseFirestoreDB.collection(COLLECTION_NAME)
                .document(uid)
                .update("name",new_name)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            if(callBack!=null)callBack.onSuccess();
                        }
                        else{
                            if(callBack!=null)callBack.onFailure();
                        }
                    }
                });
    }
}
