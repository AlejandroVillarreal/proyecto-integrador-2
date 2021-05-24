package com.example.proyecto_integrador_2.data.database.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.proyecto_integrador_2.data.database.daos.UserDao;
import com.example.proyecto_integrador_2.data.database.entities.UserEntity;

@Database(
        entities = {UserEntity.class}, //Agregar entities (tablas de firebase)
        version = 1)
public abstract class ServiceFinderDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "ServiceFinder-app.db";

    public abstract UserDao userDao();
}
