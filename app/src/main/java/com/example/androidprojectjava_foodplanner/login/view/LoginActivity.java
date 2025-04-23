package com.example.androidprojectjava_foodplanner.login.view;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.credentials.CreateCredentialRequest;
import androidx.credentials.Credential;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.exceptions.GetCredentialException;

import com.example.androidprojectjava_foodplanner.R;
import com.example.androidprojectjava_foodplanner.login.presenter.LoginPresenter;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.concurrent.Executor;

public class LoginActivity extends AppCompatActivity implements LoginViewContract{
    private LoginPresenter presenter;
    private MaterialButton signInWithGoogleBtn;
    private MaterialButton signInWithEmailBtn;
    private MaterialButton createAccountBtn;
    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;
    private TextInputEditText emailTextView;
    private TextInputEditText passwordTextView;

    private CredentialManager credentialManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        credentialManager = CredentialManager.create(this);

        presenter = LoginPresenter.getInstance();
        signInWithGoogleBtn = findViewById(R.id.loginActivityContinueWithGoogleButton);
        signInWithEmailBtn = findViewById(R.id.loginActivitySignInButton);
        createAccountBtn = findViewById(R.id.loginActivityCreateAccountButton);
        emailLayout = findViewById(R.id.emailTextInputLayout);
        passwordLayout = findViewById(R.id.passwordTextInputLayout);
        emailTextView = findViewById(R.id.LoginActivityEmailEditText);
        passwordTextView = findViewById(R.id.LoginActivityPasswordTextView);

        if(savedInstanceState != null){
            emailTextView.setText(savedInstanceState.getString("EmailKey"));
            passwordTextView.setText(savedInstanceState.getString("PasswordKey"));
        }

        signInWithGoogleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetGoogleIdOption googleIdOption = new GetGoogleIdOption.Builder()
                        .setFilterByAuthorizedAccounts(false)
                        .setServerClientId(getString(R.string.default_web_client_id))
                        .setAutoSelectEnabled(false)
                        .build();
                Log.i("defaultWeb",getString(R.string.default_web_client_id));
                GetCredentialRequest request = new GetCredentialRequest.Builder()
                        .addCredentialOption(googleIdOption)
                        .build();

                credentialManager.getCredentialAsync(
                        LoginActivity.this,
                        request,
                        null,
                        ContextCompat.getMainExecutor(LoginActivity.this),
                        new CredentialManagerCallback<GetCredentialResponse, GetCredentialException>() {
                            @Override
                            public void onResult(GetCredentialResponse getCredentialResponse) {
                                Credential credential = getCredentialResponse.getCredential();
                                String idToken = ((GoogleIdTokenCredential) credential).getIdToken();
                                presenter.LoginWithGoogleAccount(idToken);
                            }

                            @Override
                            public void onError(@NonNull GetCredentialException e) {
                                Log.i("Google_Adham_Error",e.toString());
                            }
                        }
                );
            }
        });

        signInWithEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle saveInstanceState){
        super.onSaveInstanceState(saveInstanceState);
        saveInstanceState.putString("EmailKey",emailTextView.getText().toString());
        saveInstanceState.putString("PasswordKey",passwordTextView.getText().toString());
    }

    @Override
    public Object getContext() {
        return this.getApplicationContext();
    }
}