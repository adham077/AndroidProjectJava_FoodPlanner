package com.example.androidprojectjava_foodplanner.account.login.view;

import com.example.androidprojectjava_foodplanner.account.login.presenter.LoginPresenter;

public interface LoginViewContract {
    void LoginStateActions(LoginPresenter.LoginState stateID);
    Object getContext();
}
