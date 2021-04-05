package com.example.proyecto_integrador_2.data.database.daos;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.proyecto_integrador_2.data.database.entities.UserEntity;

@Dao
public abstract class UserDao extends EntityDao<UserEntity> {
    @Query("SELECT * FROM USER_ENTITY WHERE FIREBASE_ID = :userId")
    public abstract UserEntity getUser(String userId);
}
