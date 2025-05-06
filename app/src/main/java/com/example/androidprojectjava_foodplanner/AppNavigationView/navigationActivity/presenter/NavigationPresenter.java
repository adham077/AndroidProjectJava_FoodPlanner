package com.example.androidprojectjava_foodplanner.AppNavigationView.navigationActivity.presenter;

import com.example.androidprojectjava_foodplanner.AppNavigationView.navigationActivity.view.FragComm;
import com.example.androidprojectjava_foodplanner.model.repository.UserRepository;
import com.google.firebase.firestore.auth.User;

public class NavigationPresenter {

    public enum NavigationState {
        HOME,
        SEARCH,
        CALENDAR,
        FAVOURITE,
        ACCOUNT
    }

    private static NavigationPresenter instance = null;
    private UserRepository userRepository;
    private FragComm view;

    private NavigationPresenter(UserRepository userRepository ,FragComm view){
        this.view = view;
        this.userRepository = userRepository;
    }

    public static NavigationPresenter getInstance(UserRepository userRepository ,FragComm view) {
         return instance = new NavigationPresenter(userRepository, view);
    }

    public void changeState(NavigationState state){
        switch(state){
            case HOME:
                view.showHome();
                break;
            case SEARCH:
                view.showSearch();
                break;
            case CALENDAR:
                if(userRepository.isLoggedIn())view.showCalendar();else view.showGuestProfile();
                break;
            case FAVOURITE:
                if(userRepository.isLoggedIn())view.showFavourite();else view.showGuestProfile();
                break;
            case ACCOUNT:
                if(userRepository.isLoggedIn())view.showProfile();else view.showGuestProfile();
                break;
        }
    }
}
