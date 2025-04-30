package com.example.androidprojectjava_foodplanner.account.login.presenter;

import com.example.androidprojectjava_foodplanner.account.login.view.LoginViewContract;
import com.example.androidprojectjava_foodplanner.model.repository.SyncingCallBacks;
import com.example.androidprojectjava_foodplanner.model.repository.UserRepository;
import com.example.androidprojectjava_foodplanner.remote.user.firebase.firebaseAuth.OnLoginCallBack;
import com.google.firebase.auth.FirebaseUser;

public class LoginPresenter {
    public enum LoginState{
        SUCCESS,
        INVALID_EMAIL_OR_PASSWORD,
        INVALID_USER,
        NETWORK_ERROR,
        AUTHENTICATION_FAILED
    }


    private static LoginPresenter instance = null;
    LoginViewContract view;
    UserRepository userRepository;


    private LoginPresenter(LoginViewContract view,UserRepository userRepository){
        this.view = view;
        this.userRepository = userRepository;

    }

    public static LoginPresenter getInstance(LoginViewContract view,UserRepository userRepository){
        if(instance == null){
            instance = new LoginPresenter(view,userRepository);
        }
        return instance;
    }

    public void onLogin(String email,String password){
        userRepository.login(email, password, new OnLoginCallBack() {
            @Override
            public void onSuccess(FirebaseUser user) {
                userRepository.syncLocalToRemote(new SyncingCallBacks() {
                    @Override
                    public void taskCompleted(int status) {
                        if(status == SUCCESS) {
                            view.LoginStateActions(LoginState.SUCCESS);
                        }
                        else{
                            view.LoginStateActions(LoginState.NETWORK_ERROR);
                        }
                    }
                });

            }

            @Override
            public void onFailure(int errorID) {
                switch(errorID){
                    case INVALID_EMAIL_OR_PASSWORD:
                        view.LoginStateActions(LoginState.INVALID_EMAIL_OR_PASSWORD);
                        break;
                    case INVALID_USER:
                        view.LoginStateActions(LoginState.INVALID_USER);
                        break;
                    case NETWORK_ERROR:
                        view.LoginStateActions(LoginState.NETWORK_ERROR);
                        break;
                    case AuthentiCAtION_FAILED:
                        view.LoginStateActions(LoginState.AUTHENTICATION_FAILED);
                        break;
                }
            }
        });
    }
}
