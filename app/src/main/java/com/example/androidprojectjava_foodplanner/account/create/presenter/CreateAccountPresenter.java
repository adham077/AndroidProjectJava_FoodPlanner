package com.example.androidprojectjava_foodplanner.account.create.presenter;

import com.example.androidprojectjava_foodplanner.account.create.view.CreateAccountContract;
import com.example.androidprojectjava_foodplanner.model.repository.UserRepository;
import com.example.androidprojectjava_foodplanner.remote.user.firebase.firebaseAuth.OnSignupCallBack;
import com.google.firebase.auth.FirebaseUser;

public class CreateAccountPresenter {
    public enum CreateAccountStatus{
        SUCCESS,
        EMAIL_FIELD_EMPTY,
        PASSWORD_FIELD_EMPTY,
        NAME_FIELD_EMPTY,
        INVALID_PASSWORD,
        INVALID_EMAIL,
        USER_EXISTS_ERROR,
        NETWORK_ERROR

    }


    CreateAccountContract view;
    UserRepository userRepository;

    private  static CreateAccountPresenter instance = null;

    private CreateAccountPresenter(UserRepository userRepository, CreateAccountContract view){
        this.userRepository = userRepository;
        this.view = view;
    }

    public static CreateAccountPresenter getInstance(UserRepository userRepository, CreateAccountContract view) {
        if (instance == null) {
            instance = new CreateAccountPresenter(userRepository, view);
        }
        return instance;
    }

    public void createAccount(String email, String password, String name){
        if(email.isBlank()){
            view.onCreateAccount(CreateAccountStatus.EMAIL_FIELD_EMPTY);
            return;
        }
        if(password.isBlank()){
            view.onCreateAccount(CreateAccountStatus.PASSWORD_FIELD_EMPTY);
            return;
        }
        if(name.isBlank()){
            view.onCreateAccount(CreateAccountStatus.NAME_FIELD_EMPTY);
            return;
        }



    }
}
