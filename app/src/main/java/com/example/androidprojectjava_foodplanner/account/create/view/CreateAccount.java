package com.example.androidprojectjava_foodplanner.account.create.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.androidprojectjava_foodplanner.AppNavigationView.navigationActivity.view.NavigationActivity;
import com.example.androidprojectjava_foodplanner.R;
import com.example.androidprojectjava_foodplanner.account.create.presenter.CreateAccountPresenter;
import com.example.androidprojectjava_foodplanner.local.database.MealLocalDataSource;
import com.example.androidprojectjava_foodplanner.model.repository.OperationCB;
import com.example.androidprojectjava_foodplanner.model.repository.UserRepository;
import com.example.androidprojectjava_foodplanner.remote.user.firebase.firebaseAuth.UserAuthentication;
import com.example.androidprojectjava_foodplanner.remote.user.firebase.firebaseDB.UserRemoteDataSource;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class CreateAccount extends AppCompatActivity implements CreateAccountContract{
    CreateAccountPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_account);
        createBtnHandler();
        UserRepository userRepository = UserRepository.getInstance(
                MealLocalDataSource.getInstance(this),
                UserAuthentication.getInstance(),
                UserRemoteDataSource.getInstance(this)
        );
        presenter = CreateAccountPresenter.getInstance(userRepository,this);
        userRepository.signup("adham.walaa@gmail.com", "12345678", "adham", new OperationCB() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(int errorID) {

            }
        });
    }

    public void createBtnHandler(){
        Button createBtn = findViewById(R.id.CreateActivityCreateAccountButton);
        TextInputEditText emailEditText = findViewById(R.id.CreateActivityEmailEditText);
        TextInputEditText passwordEditText = findViewById(R.id.CreateActivityPasswordTextView);
        TextInputEditText nameEditText = findViewById(R.id.CreateActivityNameTextView);

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.createAccount(
                        emailEditText.getText().toString(),
                        passwordEditText.getText().toString(),
                        nameEditText.getText().toString()
                );
            }
        });
    }

    @Override
    public void onCreateAccount(CreateAccountPresenter.CreateAccountStatus status) {
        TextInputLayout emailInputLayout1 = findViewById(R.id.CreatemailTextInputLayout);
        switch (status){
            case SUCCESS:
                Intent intent = new Intent(CreateAccount.this, NavigationActivity.class);
                intent.putExtra("guest",false);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                break;

            case INVALID_EMAIL:
                emailInputLayout1.setError("Invalid email");
                break;

            case INVALID_PASSWORD:
                TextInputLayout passwordInputLayout = findViewById(R.id.CreatepasswordTextInputLayout);
                passwordInputLayout.setError("Invalid password");
                break;

            case NAME_FIELD_EMPTY:
                TextInputLayout nameInputLayout = findViewById(R.id.CreateNameTextInputLayout);
                nameInputLayout.setError("Field can't be empty");
                break;

            case EMAIL_FIELD_EMPTY:
                emailInputLayout1.setError("Field can't be empty");
                break;

            case PASSWORD_FIELD_EMPTY:
                TextInputLayout passwordInputLayout1 = findViewById(R.id.CreatepasswordTextInputLayout);
                passwordInputLayout1.setError("Field can't be empty");
                break;

            case NETWORK_ERROR:
                Toast.makeText(this, "Unable to connect to network", Toast.LENGTH_SHORT).show();
                break;

            case USER_EXISTS_ERROR:
                emailInputLayout1 = findViewById(R.id.CreatemailTextInputLayout);
                emailInputLayout1.setError("Email already exists");
                break;
        }
    }
}