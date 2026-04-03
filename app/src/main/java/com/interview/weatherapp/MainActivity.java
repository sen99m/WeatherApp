package com.interview.weatherapp;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.interview.weatherapp.data.network.model.WeatherResponse;
import com.interview.weatherapp.ui.adapter.WeatherAdapter;
import com.interview.weatherapp.ui.viewModel.WeatherViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private WeatherViewModel weatherViewModel;
    private WeatherAdapter weatherAdapter;
    private EditText editTextCityName;
    private ProgressBar progressBar;
    private TextView errorWindow;
    private RecyclerView recyclerView;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextCityName = findViewById(R.id.etSearch);
        progressBar = findViewById(R.id.progressBar);
        errorWindow = findViewById(R.id.error);
        searchButton = findViewById(R.id.button);
        progressBar.setVisibility(View.GONE);
        setupRecyclerView();

        weatherViewModel = new ViewModelProvider(this).get(WeatherViewModel.class);

        observeViewModel();

        searchButton.setOnClickListener(v -> {
            String cityName = editTextCityName.getText().toString();
            weatherViewModel.searchCity(cityName);
            hideKeyBoard();
        });
    }

    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        weatherAdapter = new WeatherAdapter();
        recyclerView.setAdapter(weatherAdapter);
    }

    private void observeViewModel() {
        weatherViewModel.getWeatherResult().observe(this, resource -> {
            if (resource == null) return;

            switch (resource.status) {
                case LOADING:
                    progressBar.setVisibility(View.VISIBLE);
                    errorWindow.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    break;

                case SUCCESS:
                    progressBar.setVisibility(View.GONE);
                    errorWindow.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                    if(resource.data != null) {
                        weatherViewModel.addToHistory(resource.data);
//                        weatherAdapter.setWeatherData(weatherViewModel.getSearchHistory());
//                        recyclerView.scrollToPosition(0);
                        List<WeatherResponse> weatherResponses = new ArrayList<>(weatherViewModel.getSearchHistory());
                        weatherAdapter.submitList(weatherResponses);
                    }
                    break;

                case ERROR:
                    progressBar.setVisibility(View.GONE);
                    errorWindow.setVisibility(View.VISIBLE);
                    errorWindow.setText(resource.message);
                    recyclerView.setVisibility(View.VISIBLE);
                    break;
            }
        });


    }

    private void hideKeyBoard() {
        View view = this.getCurrentFocus();
        if(view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}