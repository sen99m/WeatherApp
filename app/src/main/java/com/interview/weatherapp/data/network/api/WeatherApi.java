package com.interview.weatherapp.data.network.api;

import com.interview.weatherapp.data.network.model.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {
    @GET("data/2.5/weather")
    Call<WeatherResponse> getWeather(
        @Query("q") String cityName,
        @Query("appId") String apiKey,
        @Query("units") String units
    );
}
