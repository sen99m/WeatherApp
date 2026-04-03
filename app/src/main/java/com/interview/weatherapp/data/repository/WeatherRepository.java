package com.interview.weatherapp.data.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.interview.weatherapp.BuildConfig;
import com.interview.weatherapp.data.database.WeatherDatabase;
import com.interview.weatherapp.data.database.dao.WeatherDao;
import com.interview.weatherapp.data.database.entity.WeatherEntity;
import com.interview.weatherapp.data.dto.WeatherDTO;
import com.interview.weatherapp.data.network.RetrofitClient;
import com.interview.weatherapp.data.network.api.WeatherApi;
import com.interview.weatherapp.data.network.response.WeatherResponse;
import com.interview.weatherapp.utils.Constants;
import com.interview.weatherapp.utils.Resource;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherRepository {
    private WeatherApi weatherApi;
    private WeatherDao weatherDao;
    private Executor executor = Executors.newSingleThreadExecutor();

    public WeatherRepository(Application application) {
        WeatherDatabase database = WeatherDatabase.getInstance(application);
        weatherDao = database.weatherDao();
        weatherApi = RetrofitClient.getApi();
    }

    public LiveData<Resource<WeatherDTO>> fetchWeather(String cityName) {
        MutableLiveData<Resource<WeatherDTO>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading(null));
        executor.execute(() -> {

            // Check the database FIRST
            WeatherEntity cachedWeatherEntity = weatherDao.getWeatherByCity(cityName);

            boolean isDataFresh = false;

            if (cachedWeatherEntity != null) {
                long timeSinceLastUpdate = System.currentTimeMillis() - cachedWeatherEntity.getLastUpdatedTime();
                isDataFresh = timeSinceLastUpdate < Constants.CACHE_EXPIRATION_TIME;
            }

            if (cachedWeatherEntity != null && isDataFresh) {
                WeatherDTO cachedWeather = new WeatherDTO();
                cachedWeather.setCityName(cachedWeatherEntity.getCityName());
                cachedWeather.setTemperature(cachedWeatherEntity.getTemperature());
                cachedWeather.setDescription(cachedWeatherEntity.getDescription());
                cachedWeather.setIconCode(cachedWeatherEntity.getIconCode());
                cachedWeather.setLastUpdatedTime(cachedWeatherEntity.getLastUpdatedTime());

                liveData.postValue(Resource.success(cachedWeather));
            } else {
                weatherApi.getWeather(cityName, BuildConfig.API_KEY, "metric").enqueue(new Callback<WeatherResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            executor.execute(() -> {
                                saveToDb(response.body());

                                WeatherDTO weatherDTO = new WeatherDTO();
                                weatherDTO.setCityName(response.body().getCityName());
                                weatherDTO.setTemperature(response.body().getMain().getTemperature());
                                weatherDTO.setDescription(response.body().getWeatherList().get(0).getDescription());
                                weatherDTO.setIconCode(response.body().getWeatherList().get(0).getIconCode());
                                weatherDTO.setLastUpdatedTime(System.currentTimeMillis());

                                liveData.postValue(Resource.success(weatherDTO));
                            });
                        } else {
                            liveData.postValue(Resource.error("City not found!", null));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<WeatherResponse> call, @NonNull Throwable t) {
                        liveData.postValue(Resource.error("Network failed: " + t.getMessage(), null));
                    }
                });
            }
        });
        return liveData;
    }

    private void saveToDb(WeatherResponse weatherResponse) {
        WeatherEntity weatherEntity = new WeatherEntity();
        weatherEntity.setCityName(weatherResponse.getCityName());
        weatherEntity.setTemperature(weatherResponse.getMain().getTemperature());
        weatherEntity.setDescription(weatherResponse.getWeatherList().get(0).getDescription());
        if (weatherResponse.getWeatherList() != null && !weatherResponse.getWeatherList().isEmpty()) {
            weatherEntity.setIconCode(weatherResponse.getWeatherList().get(0).getIconCode());
        }

        weatherEntity.setLastUpdatedTime(System.currentTimeMillis());

        weatherDao.insertWeather(weatherEntity);
        weatherDao.deleteOldRecords();
    }
}
