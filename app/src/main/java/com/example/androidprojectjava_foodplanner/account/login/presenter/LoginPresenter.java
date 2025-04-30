package com.example.androidprojectjava_foodplanner.account.login.presenter;
import com.example.androidprojectjava_foodplanner.account.login.view.LoginViewContract;
import com.example.androidprojectjava_foodplanner.model.repository.OperationCB;
import com.example.androidprojectjava_foodplanner.model.repository.UserRepository;

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
        userRepository.login(email, password, new OperationCB() {
            @Override
            public void onSuccess() {
                view.LoginStateActions(LoginState.SUCCESS);
            }

            @Override
            public void onFailure(int errorID) {
                switch(errorID){
                    case OperationCB.INVALID_EMAIL_OR_PASSWORD:
                        view.LoginStateActions(LoginState.INVALID_EMAIL_OR_PASSWORD);
                        break;
                    case OperationCB.INVALID_USER:
                        view.LoginStateActions(LoginState.INVALID_USER);
                        break;
                    case OperationCB.NETWORK_ERROR:
                        view.LoginStateActions(LoginState.NETWORK_ERROR);
                        break;

                    case OperationCB.AuthentiCAtION_FAILED:
                        view.LoginStateActions(LoginState.AUTHENTICATION_FAILED);
                        break;
                }
            }
        });
    }
}
