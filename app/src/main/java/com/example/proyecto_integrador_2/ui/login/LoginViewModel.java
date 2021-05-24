package com.example.proyecto_integrador_2.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.proyecto_integrador_2.data.database.entities.UserEntity;
import com.example.proyecto_integrador_2.data.network.services.LoginResult;
import com.example.proyecto_integrador_2.domain.user.UsersManager;
import com.example.proyecto_integrador_2.domain.user.exception.EmailValidationException;
import com.example.proyecto_integrador_2.domain.user.exception.PasswordValidationException;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class LoginViewModel extends ViewModel implements LoginResult {

    private final UsersManager usersManager;
    private MutableLiveData<Throwable> emailError;
    private MutableLiveData<Throwable> passwordError;
    private MutableLiveData<Throwable> signInError;
    private MutableLiveData<Throwable> getUserError;
    private MutableLiveData<Boolean> signInSuccessful;

    @Inject
    public LoginViewModel(UsersManager usersManager) {
        this.usersManager = usersManager;
        emailError = new MutableLiveData<>();
        passwordError = new MutableLiveData<>();
        signInError = new MutableLiveData<>();
        getUserError = new MutableLiveData<>();
        signInSuccessful = new MutableLiveData<>();
    }

    public void connect(String email, String password) {
        boolean hasError = false;
        if (usersManager.emailIsInvalid(email)) {
            emailError.postValue(new EmailValidationException());
            hasError = true;
        }
        if (usersManager.passwordIsInvalid(password)) {
            passwordError.postValue(new PasswordValidationException());
            hasError = true;
        }
        if (hasError) {
            return;
        }
        usersManager.signIn(email, password, this);
    }

    @Override
    public void onSignInSuccessful(String userId) {
        usersManager.getUserInfo(userId, this);
    }

    @Override
    public void onSignInFailure(Throwable error) {
        signInError.postValue(error);
    }

    @Override
    public void onGetUserSuccessful(UserEntity userEntity) {
        usersManager.saveUser(userEntity);
        signInSuccessful.postValue(true);
    }

    @Override
    public void onGetUserFailure(Throwable error) {
        getUserError.postValue(error);
    }

    public LiveData<Throwable> getEmailError() {
        return emailError;
    }

    public LiveData<Throwable> getPasswordError() {
        return passwordError;
    }

    public LiveData<Throwable> getSignInError() {
        return signInError;
    }

    public LiveData<Throwable> getGetUserError() {
        return getUserError;
    }

    public LiveData<Boolean> getSignInSuccessful() {
        return signInSuccessful;
    }
}
