package com.interview.weatherapp.ui.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.interview.weatherapp.data.dto.WeatherDTO;
import com.interview.weatherapp.data.repository.WeatherRepository;
import com.interview.weatherapp.utils.Resource;

import java.util.ArrayList;
import java.util.List;

public class WeatherViewModel extends AndroidViewModel {
    private WeatherRepository weatherRepository;

    private MutableLiveData<String> searchTrigger = new MutableLiveData<>();

    private LiveData<Resource<WeatherDTO>> weatherResult;

    private List<WeatherDTO> searchHistory = new ArrayList<>();

    public WeatherViewModel(@NonNull Application application) {
        super(application);
        weatherRepository = new WeatherRepository(application);
        weatherResult = Transformations.switchMap(searchTrigger, cityName -> {
            return weatherRepository.fetchWeather(cityName);
        });
    }

    public void searchCity(String cityName) {
        if (cityName != null && !cityName.trim().isEmpty()) {
            searchTrigger.setValue(cityName.trim());
        }
    }

    public LiveData<Resource<WeatherDTO>> getWeatherResult() {
        return weatherResult;
    }

    public void addToHistory(WeatherDTO newCity) {
        if (newCity == null) return;

        searchHistory.removeIf(city ->
                city.getCityName().equalsIgnoreCase(newCity.getCityName())
        );

        searchHistory.add(0, newCity);

        if (searchHistory.size() > 25) {
            searchHistory.remove(searchHistory.size() - 1); // Remove the oldest at the bottom
        }
    }

    public List<WeatherDTO> getSearchHistory() {
        return searchHistory;
    }
}
