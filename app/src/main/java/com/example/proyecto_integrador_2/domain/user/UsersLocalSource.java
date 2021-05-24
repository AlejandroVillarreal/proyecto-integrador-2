package com.example.proyecto_integrador_2.domain.user;

import com.example.proyecto_integrador_2.data.database.daos.UserDao;
import com.example.proyecto_integrador_2.data.database.entities.UserEntity;

import java.util.concurrent.Executor;

import javax.inject.Inject;

public class UsersLocalSource {
    private UserDao userDao;

    @Inject
    public UsersLocalSource(UserDao userDao, Executor executor) {
        this.userDao = userDao;
    }

    public void saveUser(UserEntity userEntity) {
        userDao.insert(userEntity);
    }

    public UserEntity getUser(String userId) {
        return userDao.getUser(userId);
    }
}
