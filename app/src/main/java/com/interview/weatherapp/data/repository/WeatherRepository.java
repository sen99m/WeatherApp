package com.interview.weatherapp.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.interview.weatherapp.data.database.WeatherDatabase;
import com.interview.weatherapp.data.database.dao.WeatherDao;
import com.interview.weatherapp.data.database.model.WeatherEntity;
import com.interview.weatherapp.data.network.model.WeatherResponse;
import com.interview.weatherapp.data.network.RetrofitClient;
import com.interview.weatherapp.data.network.api.WeatherApi;
import com.interview.weatherapp.utils.Constants;
import com.interview.weatherapp.utils.Resource;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherRepository {
    private WeatherApi weatherApi;
    private WeatherDao weatherDao;
    private Executor executor = Executors.newSingleThreadExecutor();

    public WeatherRepository() {
//        WeatherDatabase database = WeatherDatabase.getInstance(application);
//        weatherDao = database.weatherDao();
        weatherApi = RetrofitClient.getApi();
    }

    public LiveData<List<WeatherEntity>> getRecentCities() {
        return weatherDao.getRecentDetails();
    }


    public LiveData<Resource<WeatherResponse>> fetchWeather(String cityName) {
        MutableLiveData<Resource<WeatherResponse>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading(null));
        weatherApi.getWeather(cityName, Constants.API_KEY, "metric").enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if(response.isSuccessful() && response.body() != null) {
//                    saveToDb(response.body());
                    liveData.setValue(Resource.success(response.body()));
                } else {
                    liveData.setValue(Resource.error("City not found!", null));
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                liveData.setValue(Resource.error("Network failed: " + t.getMessage(), null));
            }
        });

        return liveData;
    }

//    private void saveToDb(WeatherResponse weatherResponse) {
//        WeatherEntity weatherEntity = new WeatherEntity();
//        weatherEntity.setCityName(weatherResponse.getCityName());
//        weatherEntity.setTemperature(weatherResponse.getMain().getTemperature());
//
//        if(weatherResponse.getWeatherList() != null && !weatherResponse.getWeatherList().isEmpty()) {
//            weatherEntity.setIconCode(weatherResponse.getWeatherList().get(0).getIconCode());
//        }
//
//        weatherEntity.setLastUpdated(System.currentTimeMillis());
//
//        weatherDao.insertWeather(weatherEntity);
//        weatherDao.deleteOldRecords();
//    }
}
