package com.example.proyecto_integrador_2.ui.register;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.proyecto_integrador_2.data.database.entities.UserEntity;
import com.example.proyecto_integrador_2.data.network.services.RegisterResult;
import com.example.proyecto_integrador_2.domain.user.UsersManager;
import com.example.proyecto_integrador_2.domain.user.exception.EmailValidationException;
import com.example.proyecto_integrador_2.domain.user.exception.NameValidationException;
import com.example.proyecto_integrador_2.domain.user.exception.PasswordValidationException;
import com.example.proyecto_integrador_2.domain.user.exception.PhoneValidationException;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class RegisterViewModel extends ViewModel implements RegisterResult {
    private final UsersManager usersManager;
    private MutableLiveData<Throwable> emailError;
    private MutableLiveData<Throwable> passwordError;
    private MutableLiveData<Throwable> nameError;
    private MutableLiveData<Throwable> phoneError;
    private MutableLiveData<Throwable> registerError;
    private MutableLiveData<Boolean> registerSuccessful;

    @Inject
    public RegisterViewModel(UsersManager usersManager) {
        this.usersManager = usersManager;
        emailError = new MutableLiveData<>();
        passwordError = new MutableLiveData<>();
        nameError = new MutableLiveData<>();
        phoneError = new MutableLiveData<>();
        registerError = new MutableLiveData<>();
        registerSuccessful = new MutableLiveData<>();
    }

    public void register(String email, String password, String name, String phone) {
        UserEntity userEntity = new UserEntity();
        userEntity.email = email;
        userEntity.name = name;
        userEntity.phone = phone;
        boolean hasError = false;
        if (usersManager.emailIsInvalid(userEntity.email)) {
            emailError.postValue(new EmailValidationException());
            hasError = true;
        }
        if (usersManager.passwordIsInvalid(password)) {
            passwordError.postValue(new PasswordValidationException());
            hasError = true;
        }
        if (usersManager.nameIsInvalid(userEntity.name)) {
            nameError.postValue(new NameValidationException());
            hasError = true;
        }
        if (usersManager.phoneIsInvalid(userEntity.phone)) {
            phoneError.postValue(new PhoneValidationException());
            hasError = true;
        }
        if (hasError) {
            return;
        }
        usersManager.createUser(userEntity, password, this);
    }

    @Override
    public void onRegisterSuccessful(UserEntity userEntity) {
        usersManager.saveUser(userEntity);
        registerSuccessful.postValue(true);
    }

    @Override
    public void onRegisterFailure(Throwable error) {
        registerError.postValue(error);
    }

    public LiveData<Throwable> getEmailError() {
        return emailError;
    }

    public LiveData<Throwable> getPasswordError() {
        return passwordError;
    }

    public LiveData<Throwable> getNameError() {
        return nameError;
    }

    public LiveData<Throwable> getPhoneError() {
        return phoneError;
    }

    public LiveData<Throwable> getRegisterError() {
        return registerError;
    }

    public LiveData<Boolean> getRegisterSuccessful() {
        return registerSuccessful;
    }
}
