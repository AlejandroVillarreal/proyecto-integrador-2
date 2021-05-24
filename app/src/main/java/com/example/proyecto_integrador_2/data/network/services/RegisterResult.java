package com.example.proyecto_integrador_2.data.network.services;

import com.example.proyecto_integrador_2.data.database.entities.UserEntity;

public interface RegisterResult {
    public void onRegisterSuccessful(UserEntity userEntity);

    public void onRegisterFailure(Throwable error);
}
