package com.example.proyecto_integrador_2.ui.home;

import com.example.proyecto_integrador_2.data.database.entities.UserEntity;

public interface ProfileInterface {

    public void profileClicked(UserEntity userEntity);

    public void sendMessage(UserEntity userEntity);
}
