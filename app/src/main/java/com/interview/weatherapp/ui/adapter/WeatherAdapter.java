package com.interview.weatherapp.ui.adapter;

import static com.interview.weatherapp.utils.Constants.IMAGE_URL;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.interview.weatherapp.R;
import com.interview.weatherapp.data.network.model.WeatherResponse;
import com.interview.weatherapp.ui.viewModel.WeatherViewModel;
import com.interview.weatherapp.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class WeatherAdapter extends ListAdapter<WeatherResponse, WeatherAdapter.WeatherViewHolder> {

    public WeatherAdapter() {
        super(new WeatherDiffCallback());
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_weather, parent, false);

        return new WeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        WeatherResponse weatherResponse = getItem(position);

        holder.cityName.setText(weatherResponse.getCityName());

        if(weatherResponse.getMain() != null) {
            String tempString = weatherResponse.getMain().getTemperature() + "\u00B0 C";
            holder.temperature.setText(tempString);
        }

        if(weatherResponse.getWeatherList() != null && !weatherResponse.getWeatherList().isEmpty()) {
            String desc = weatherResponse.getWeatherList().get(0).getDescription();
            holder.description.setText(desc);

            String iconCode = weatherResponse.getWeatherList().get(0).getIconCode();
            String imageUrl = Constants.IMAGE_URL + iconCode + "@2x.png";
            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(holder.ivWeatherIcon);

        }
    }

    static class WeatherDiffCallback extends DiffUtil.ItemCallback<WeatherResponse> {
        @Override
        public boolean areItemsTheSame(@NonNull WeatherResponse oldItem, @NonNull WeatherResponse newItem) {
            return oldItem.getCityName().equals(newItem.getCityName());
        }

        @Override
        public boolean areContentsTheSame(@NonNull WeatherResponse oldItem, @NonNull WeatherResponse newItem) {
            if(oldItem.getMain() == null || newItem.getMain() == null) {
                return false;
            }
            return oldItem.getMain().getTemperature() == newItem.getMain().getTemperature();
        }

    }

    static class WeatherViewHolder extends RecyclerView.ViewHolder {
        TextView cityName;
        TextView temperature;
        TextView description;
        ImageView ivWeatherIcon;

        TextView lastUpdatedTime;

        public WeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            cityName = itemView.findViewById(R.id.cityName);
            temperature = itemView.findViewById(R.id.temperature);
            description = itemView.findViewById(R.id.description);
            ivWeatherIcon = itemView.findViewById(R.id.ivWeatherIcon);
            lastUpdatedTime = itemView.findViewById(R.id.lastUpdatedTime);
        }
    }
}
