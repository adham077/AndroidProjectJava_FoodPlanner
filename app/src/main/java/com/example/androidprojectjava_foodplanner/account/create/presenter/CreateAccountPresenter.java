package com.example.androidprojectjava_foodplanner.account.create.presenter;

import com.example.androidprojectjava_foodplanner.account.create.view.CreateAccountContract;
import com.example.androidprojectjava_foodplanner.model.repository.UserRepository;

public class CreateAccountPresenter {
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


}
