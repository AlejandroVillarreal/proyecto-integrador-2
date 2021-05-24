package com.example.proyecto_integrador_2.data.database.daos;

import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

public abstract class EntityDao<E> {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract long insert(E entity);
}
