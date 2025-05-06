package com.example.androidprojectjava_foodplanner.account.login.view;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
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
import androidx.annotation.Nullable;
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
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

public class LoginActivity extends AppCompatActivity implements LoginViewContract{
    private LoginPresenter presenter;
    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;
    private static final int REQ_ONE_TAP = 1001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        UserRepository userRepository = UserRepository.getInstance(
                MealLocalDataSource.getInstance(this),
                MealRemoteDataSource.getInstance(this),
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
                showLoading();
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if(email == null || email.isEmpty()){
                    hideLoading();
                    emailInputLayout.setError("Email is empty");
                    return;
                }

                if(password==null ||  password.isEmpty()){
                    hideLoading();
                    passwordInputLayout.setError("Password is empty");
                    return;
                }

                presenter.onLogin(email,password);
            }
        });

    }

    private void showLoading(){
        findViewById(R.id.loginLoadingView).setVisibility(View.VISIBLE);
        findViewById(R.id.loginView).setVisibility(View.GONE);
    }
    private void hideLoading(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.loginLoadingView).setVisibility(View.GONE);
                findViewById(R.id.loginView).setVisibility(View.VISIBLE);
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
    public void continueWithGoogleHandler(){
        oneTapClient = Identity.getSignInClient(this);
        signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId(getString(R.string.default_web_client_id))
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                .setAutoSelectEnabled(true)
                .build();


        MaterialButton continueWithGoogleBtn = findViewById(R.id.loginActivityContinueWithGoogleButton);
        continueWithGoogleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignIn();
            }
        });
    }

    public void startSignIn(){
        oneTapClient.beginSignIn(signInRequest).addOnSuccessListener(this, new OnSuccessListener<BeginSignInResult>() {
            @Override
            public void onSuccess(BeginSignInResult beginSignInResult) {
                try{
                    startIntentSenderForResult(
                            beginSignInResult.getPendingIntent().getIntentSender(),
                            REQ_ONE_TAP,
                            null,
                            0,0,0);

                }
                catch (IntentSender.SendIntentException e) {
                    throw new RuntimeException(e);
                }
            }
        })
        .addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        try{
            SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
            String idToken = credential.getGoogleIdToken();
            presenter.onSignInWithGoogle(idToken);

        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    public void LoginStateActions(LoginPresenter.LoginState stateID) {
        hideLoading();
        TextInputLayout emailInputLayout = findViewById(R.id.emailTextInputLayout);
        TextInputLayout passwordInputLayout = findViewById(R.id.passwordTextInputLayout);

        switch(stateID){
            case SUCCESS:
                Intent intent = new Intent(LoginActivity.this,NavigationActivity.class);
                intent.putExtra("guest",false);
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