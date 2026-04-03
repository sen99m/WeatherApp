package com.interview.weatherapp.data.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherResponse {
    @SerializedName("name")
    private String cityName;

    @SerializedName("main")
    private MainWeather main;

    @SerializedName("weather")
    private List<WeatherDescription> weatherList;

    public static class MainWeather {
        @SerializedName("temp")
        private double temperature;

        @SerializedName("humidity")
        private int humidity;

        public double getTemperature() {
            return temperature;
        }
        public int getHumidity() {
            return humidity;
        }
    }

    public static class WeatherDescription {
        @SerializedName("description")
        private String description;

        @SerializedName("icon")
        private String iconCode;

        public String getDescription() {
            return description;
        }

        public String getIconCode() {
            return iconCode;
        }
    }

    public String getCityName() {
        return cityName;
    }

    public MainWeather getMain() {
        return main;
    }

    public List<WeatherDescription> getWeatherList() {
        return weatherList;
    }
}
