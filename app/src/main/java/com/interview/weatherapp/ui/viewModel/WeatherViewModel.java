package com.interview.weatherapp.ui.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.interview.weatherapp.data.network.model.WeatherResponse;
import com.interview.weatherapp.data.repository.WeatherRepository;
import com.interview.weatherapp.utils.Resource;

import java.util.ArrayList;
import java.util.List;

public class WeatherViewModel extends ViewModel {
    private WeatherRepository weatherRepository;

    private MutableLiveData<String> searchTrigger = new MutableLiveData<>();

    private LiveData<Resource<WeatherResponse>> weatherResult;

    private List<WeatherResponse> searchHistory = new ArrayList<>();
    public WeatherViewModel() {
        weatherRepository = new WeatherRepository();

        weatherResult = Transformations.switchMap(searchTrigger, cityName -> {
            return weatherRepository.fetchWeather(cityName);
        });

    }

    public void searchCity(String cityName) {
        if(cityName != null && !cityName.trim().isEmpty()) {
            searchTrigger.setValue(cityName.trim());
        }
    }

    public LiveData<Resource<WeatherResponse>> getWeatherResult() {
        return weatherResult;
    }

    public void addToHistory(WeatherResponse weatherResponse) {
        if(weatherResponse == null)
            return;

        searchHistory.removeIf(pastWeatherResponse ->
                pastWeatherResponse.getCityName().equalsIgnoreCase(weatherResponse.getCityName())
        );

        searchHistory.add(0, weatherResponse);

        if(searchHistory.size() > 25) {
            searchHistory.remove(searchHistory.size() - 1);
        }
    }

    public List<WeatherResponse> getSearchHistory() {
        return searchHistory;
    }
}
