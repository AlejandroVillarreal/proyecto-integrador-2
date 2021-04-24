package com.example.proyecto_integrador_2.data.mappers;

import com.example.proyecto_integrador_2.data.database.entities.UserEntity;

import java.util.HashMap;

import javax.inject.Inject;

public class UserMapper {

    @Inject
    public UserMapper() {

    }

    public UserEntity map(HashMap<String, String> values) {
        UserEntity user = new UserEntity();
        user.user_id = values.get("user_id");
        user.name = values.get("name");
        user.email = values.get("email");
        user.phone = values.get("phone");
        user.profile_pic = values.get("profile_pic");
        return user;
    }
}
