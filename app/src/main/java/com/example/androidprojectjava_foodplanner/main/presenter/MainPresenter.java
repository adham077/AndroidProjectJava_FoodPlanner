package com.example.androidprojectjava_foodplanner.main.presenter;

import com.example.androidprojectjava_foodplanner.main.view.MainActivityContract;
import com.example.androidprojectjava_foodplanner.model.repository.UserRepository;

public class MainPresenter {
    private UserRepository userRepository;
    private MainActivityContract view;
    private static MainPresenter instance = null;
    private MainPresenter(UserRepository userRepository,MainActivityContract view){
        this.view = view;
        this.userRepository = userRepository;
    }

    public static MainPresenter getInstance(UserRepository userRepository,MainActivityContract view){
        if(instance == null){
            instance = new MainPresenter(userRepository,view);
        }
        return instance;
    }

    public void init(){
        if(userRepository.isLoggedIn()){
            view.init(MainActivityContract.UserState.LOGGED_IN);
        }
        else{
            view.init(MainActivityContract.UserState.NOT_LOGGED_IN);
        }
    }

}
