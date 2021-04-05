package com.example.proyecto_integrador_2.data.network.services;

import com.example.proyecto_integrador_2.data.database.entities.UserEntity;

public interface LoginResult {

    public void onSignInSuccessful(String userId);

    public void onSignInFailure(Throwable error);

    public void onGetUserSuccessful(UserEntity userEntity);

    public void onGetUserFailure(Throwable error);
}


