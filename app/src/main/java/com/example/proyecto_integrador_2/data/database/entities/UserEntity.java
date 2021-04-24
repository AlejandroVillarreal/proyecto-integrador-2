package com.example.proyecto_integrador_2.data.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "USER_ENTITY")
public class UserEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "FIREBASE_ID")
    public String user_id;
    @ColumnInfo(name = "EMAIL")
    public String email;
    @ColumnInfo(name = "NAME")
    public String name;
    @ColumnInfo(name = "PHONE")
    public String phone;
    @ColumnInfo(name = "PICTURE")
    public String profile_pic;
}
