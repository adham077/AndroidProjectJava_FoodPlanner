package com.example.androidprojectjava_foodplanner.AppNavigationView.profile.presenter;

import com.example.androidprojectjava_foodplanner.AppNavigationView.profile.view.ProfileContract;
import com.example.androidprojectjava_foodplanner.model.repository.UserRepository;
import com.example.androidprojectjava_foodplanner.remote.user.firebase.firebaseAuth.UserAuthentication;

public class ProfilePresenter {
    private ProfileContract view;
    private UserRepository userRepository;
    private static ProfilePresenter instance = null;

    private ProfilePresenter(ProfileContract view, UserRepository userRepository) {
        this.view = view;
        this.userRepository = userRepository;
    }

    public static ProfilePresenter getInstance(ProfileContract view, UserRepository userRepository) {
        if(instance == null){
            instance = new ProfilePresenter(view,userRepository);
        }
        return instance;
    }

    public void logout(){
        userRepository.logout();
        view.goToLoginHome();
    }

    public void init(){
        String userName = userRepository.getUserDisplayName();
        view.init(userName);
    }
}
