package com.example.androidprojectjava_foodplanner.remote.firebase.firebaseAuth;

import com.google.firebase.Firebase;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserRemoteDataSource {
    public UserRemoteDataSource(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
    }
}
