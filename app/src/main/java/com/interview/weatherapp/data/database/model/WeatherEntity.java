package com.interview.weatherapp.data.database.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "weather")
public class WeatherEntity {
    @PrimaryKey
    @NonNull
    private String cityName;
    private double temperature;
    private String description;
    private String iconCode;
    private long lastUpdatedTime;

    @NonNull
    public String getCityName() {
        return cityName;
    }

    public void setCityName(@NonNull String cityName) {
        this.cityName = cityName;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIconCode() {
        return iconCode;
    }

    public void setIconCode(String iconCode) {
        this.iconCode = iconCode;
    }

    public long getLastUpdated() {
        return lastUpdatedTime;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdatedTime = lastUpdatedTime;
    }
}
