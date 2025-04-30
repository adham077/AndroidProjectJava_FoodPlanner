package com.example.androidprojectjava_foodplanner.account.login.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.androidprojectjava_foodplanner.AppNavigationView.navigationActivity.view.NavigationActivity;
import com.example.androidprojectjava_foodplanner.R;
import com.example.androidprojectjava_foodplanner.account.create.view.CreateAccount;
import com.example.androidprojectjava_foodplanner.local.database.MealLocalDataSource;
import com.example.androidprojectjava_foodplanner.account.login.presenter.LoginPresenter;
import com.example.androidprojectjava_foodplanner.model.pojo.FavouriteMeal;
import com.example.androidprojectjava_foodplanner.model.pojo.Meal;
import com.example.androidprojectjava_foodplanner.model.pojo.PlannedMeal;
import com.example.androidprojectjava_foodplanner.model.repository.OperationCB;
import com.example.androidprojectjava_foodplanner.model.repository.SyncingCallBacks;
import com.example.androidprojectjava_foodplanner.model.repository.UserRepository;
import com.example.androidprojectjava_foodplanner.remote.meal.MealNetworkCB;
import com.example.androidprojectjava_foodplanner.remote.meal.MealRemoteDataSource;
import com.example.androidprojectjava_foodplanner.remote.user.firebase.firebaseAuth.UserAuthentication;
import com.example.androidprojectjava_foodplanner.remote.user.firebase.firebaseDB.UserRemoteDataSource;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

public class LoginActivity extends AppCompatActivity implements LoginViewContract{
    LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        UserRepository userRepository = UserRepository.getInstance(
                MealLocalDataSource.getInstance(this),
                UserAuthentication.getInstance(),
                UserRemoteDataSource.getInstance(this)
        );

        presenter = LoginPresenter.getInstance(this,userRepository);


        continueAsGuestHandler();
        loginHandler();
        createAccountHandler();
        continueWithGoogleHandler();
    }

    public void continueAsGuestHandler(){
        MaterialTextView continueAsaGuestTextView = findViewById(R.id.continueAsaGuestTextView);
        String fullText = "Not registered? Continue as a guest";
        SpannableString spannable = new SpannableString(fullText);
        String clickableText = "Continue as a guest";
        int clickableStart = fullText.indexOf(clickableText);
        int clickableEnd = clickableStart + clickableText.length();
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent = new Intent(LoginActivity.this, NavigationActivity.class);
                intent.putExtra("guest",true);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(ContextCompat.getColor(LoginActivity.this, R.color.colorPrimary));
                ds.setUnderlineText(false);
            }
        };

        spannable.setSpan(
                clickableSpan,
                clickableStart,
                clickableEnd,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        continueAsaGuestTextView.setText(spannable);
        continueAsaGuestTextView.setMovementMethod(LinkMovementMethod.getInstance());

    }

    public void loginHandler(){
        TextInputLayout emailInputLayout = findViewById(R.id.emailTextInputLayout);
        TextInputLayout passwordInputLayout = findViewById(R.id.passwordTextInputLayout);
        TextInputEditText emailEditText = findViewById(R.id.LoginActivityEmailEditText);
        TextInputEditText passwordEditText = findViewById(R.id.LoginActivityPasswordTextView);
        MaterialButton loginButton = findViewById(R.id.loginActivitySignInButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if(email == null || email.isEmpty()){
                    emailInputLayout.setError("Email is empty");
                    return;
                }

                if(password==null ||  password.isEmpty()){
                    passwordInputLayout.setError("Password is empty");
                    return;
                }

                presenter.onLogin(email,password);
            }
        });

    }

    public void createAccountHandler(){
        MaterialButton createAccountBtn = findViewById(R.id.loginActivityCreateAccountButton);
        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, CreateAccount.class);
                startActivity(intent);
            }
        });
    }
    public void continueWithGoogleHandler(){}
    @Override
    public void LoginStateActions(LoginPresenter.LoginState stateID) {
        TextInputLayout emailInputLayout = findViewById(R.id.emailTextInputLayout);
        TextInputLayout passwordInputLayout = findViewById(R.id.passwordTextInputLayout);

        switch(stateID){
            case SUCCESS:
                Intent intent = new Intent(LoginActivity.this,NavigationActivity.class);
                intent.putExtra("guest",false);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                break;
            case INVALID_EMAIL_OR_PASSWORD:
                emailInputLayout.setError("Invalid email or password");
                passwordInputLayout.setError("Invalid email or password");
                break;
            case INVALID_USER:
                emailInputLayout.setError("Invalid user");
                passwordInputLayout.setError("Invalid user");
                break;
            case NETWORK_ERROR:
                emailInputLayout.setError("Network error");
                passwordInputLayout.setError("Network error");
                break;
            case AUTHENTICATION_FAILED:
                emailInputLayout.setError("Authentication failed");
                passwordInputLayout.setError("Authentication failed");
                break;
        }
    }

    @Override
    public Object getContext() {
        return this.getApplicationContext();
    }
}