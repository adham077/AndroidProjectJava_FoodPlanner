package com.example.androidprojectjava_foodplanner.remote.user.firebase.firebaseDB;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.androidprojectjava_foodplanner.model.pojo.PlannerUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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

}
