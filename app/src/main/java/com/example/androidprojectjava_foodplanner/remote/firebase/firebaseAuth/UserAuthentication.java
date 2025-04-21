package com.example.androidprojectjava_foodplanner.remote.firebase.firebaseAuth;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

public class UserAuthentication {
    private FirebaseAuth firebaseAuth;
    private static UserAuthentication instance = null;
    private UserAuthentication(){
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public static UserAuthentication getInstance(){
        if(instance == null){
            instance = new UserAuthentication();
        }
        return instance;
    }

    public void login(String email, String password, OnLoginCallBack callBack){
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    callBack.onSuccess(firebaseAuth.getCurrentUser());
                }
                else{
                    try{
                        throw Objects.requireNonNull(task.getException());
                    }
                    catch(FirebaseNetworkException ex){
                        callBack.onFailure(OnLoginCallBack.NETWORK_ERROR);
                    }
                    catch (FirebaseAuthInvalidCredentialsException ex) {
                        callBack.onFailure(OnLoginCallBack.INVALID_EMAIL_OR_PASSWORD);
                    }
                    catch(FirebaseAuthInvalidUserException ex){
                        callBack.onFailure(OnLoginCallBack.INVALID_USER);
                    }
                    catch(Exception e){
                        callBack.onFailure(OnLoginCallBack.AuthentiCAtION_FAILED);
                    }
                }
            }
        });
    }

    public void logOut(){
        firebaseAuth.signOut();
    }

    public void deleteCurrentUser(OnDeletionCallBack callBack){
        Objects.requireNonNull(firebaseAuth.getCurrentUser()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    callBack.onSuccess();
                }
                else{
                    callBack.onFailure();
                }
            }
        });
    }

    public void signUp(String email,String password,OnSignupCallBack callBack){
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    callBack.onSuccess(firebaseAuth.getCurrentUser());
                }
                else{
                    try{
                        throw Objects.requireNonNull(task.getException());
                    }
                    catch(FirebaseNetworkException ex){
                        callBack.onFailure(OnSignupCallBack.NETWORK_ERROR);
                    }
                    catch (FirebaseAuthEmailException ex){
                        callBack.onFailure(OnSignupCallBack.INVALID_EMAIL);
                    }
                    catch(FirebaseAuthWeakPasswordException ex){
                        callBack.onFailure(OnSignupCallBack.PASSWORD_LENGTH_ERROR);
                    }
                    catch(FirebaseAuthUserCollisionException ex){
                        callBack.onFailure(OnSignupCallBack.USER_EXISTS_ERROR);
                    } catch(Exception e){
                        callBack.onFailure(OnLoginCallBack.AuthentiCAtION_FAILED);
                    }
                }
            }
        });
    }

    public void signInWithGoogleAccount(String ID_Token, OnLoginCallBack callBack){
        AuthCredential credential = GoogleAuthProvider.getCredential(ID_Token,null);
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    callBack.onSuccess(firebaseAuth.getCurrentUser());
                }
                else{
                    try{
                        throw Objects.requireNonNull(task.getException());
                    }
                    catch(FirebaseNetworkException ex){
                        callBack.onFailure(OnLoginCallBack.NETWORK_ERROR);
                    }
                    catch (FirebaseAuthInvalidCredentialsException ex) {
                        callBack.onFailure(OnLoginCallBack.INVALID_EMAIL_OR_PASSWORD);
                    }
                    catch(FirebaseAuthInvalidUserException ex){
                        callBack.onFailure(OnLoginCallBack.INVALID_USER);
                    }
                    catch(Exception e){
                        callBack.onFailure(OnLoginCallBack.AuthentiCAtION_FAILED);
                    }
                }
            }
        });
    }
    public FirebaseUser getCurrentUser(){
        return firebaseAuth.getCurrentUser();
    }
}
