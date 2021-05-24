package com.example.proyecto_integrador_2.domain.user;

import com.example.proyecto_integrador_2.data.database.entities.UserEntity;
import com.example.proyecto_integrador_2.data.network.services.LoginResult;
import com.example.proyecto_integrador_2.data.network.services.LoginService;
import com.example.proyecto_integrador_2.data.network.services.RegisterResult;
import com.example.proyecto_integrador_2.data.network.services.RegisterService;

import javax.inject.Inject;

public class UsersRemoteSource {

    private LoginService loginService;
    private RegisterService registerService;

    @Inject
    public UsersRemoteSource(LoginService loginService, RegisterService registerService) {
        this.loginService = loginService;
        this.registerService = registerService;
    }

    public void signIn(String email, String password, LoginResult loginResult) {

        loginService.signIn(email, password, loginResult);
    }

    public void getUserInfo(String userId, LoginResult loginResult) {
        loginService.getUserInfo(userId, loginResult);
    }

    public void createUser(UserEntity userEntity, String password, RegisterResult registerResult) {
        registerService.createUser(userEntity, password, registerResult);
    }
}
