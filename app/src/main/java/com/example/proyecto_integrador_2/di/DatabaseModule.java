package com.example.proyecto_integrador_2.di;

import android.content.Context;

import androidx.room.Room;

import com.example.proyecto_integrador_2.data.database.daos.UserDao;
import com.example.proyecto_integrador_2.data.database.db.ServiceFinderDatabase;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public abstract class DatabaseModule {

    @Provides
    @Singleton
    public static Executor provideExecutor() {
        return Executors.newFixedThreadPool(5);
    }

    @Provides
    @Singleton
    public static ServiceFinderDatabase provideDatabase(
            @ApplicationContext Context context,
            Executor roomExecutor
    ) {
        return Room.databaseBuilder(context, ServiceFinderDatabase.class, ServiceFinderDatabase.DATABASE_NAME)
                .setQueryExecutor(roomExecutor)
                .build();

    }

    //Por cada tabla se necesita un Dao
    @Provides
    public static UserDao provideUserDao(ServiceFinderDatabase database) {
        return database.userDao();
    }
}
