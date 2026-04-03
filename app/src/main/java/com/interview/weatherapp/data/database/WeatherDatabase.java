package com.interview.weatherapp.data.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.interview.weatherapp.data.database.dao.WeatherDao;
import com.interview.weatherapp.data.database.entity.WeatherEntity;

@Database(entities = {WeatherEntity.class}, version = 1, exportSchema = true)
public abstract class WeatherDatabase extends RoomDatabase {
    public abstract WeatherDao weatherDao();

    private static volatile WeatherDatabase instance;

    private static final String DATABASE_NAME = "weather_database";

    public static WeatherDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (WeatherDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    WeatherDatabase.class,
                                    DATABASE_NAME
                            )
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return instance;
    }
}
