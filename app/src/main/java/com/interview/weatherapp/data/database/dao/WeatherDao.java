package com.interview.weatherapp.data.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.interview.weatherapp.data.database.model.WeatherEntity;

import java.util.List;

public interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertWeather(WeatherEntity weatherEntity);

    @Query("SELECT * FROM weather WHERE cityName = :cityName")
    WeatherEntity getWeatherByCity(String cityName);

    @Query("SELECT * FROM weather ORDER BY lastUpdated DESC LIMIT 25")
    LiveData<List<WeatherEntity>> getRecentDetails();

    @Query("DELETE FROM weather WHERE cityName NOT IN " +
            "(SELECT cityName FROM weather ORDER BY lastUpdated DESC LIMIT 25)")
    void deleteOldRecords();
}
