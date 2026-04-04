package com.interview.weatherapp.ui.adapter;

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
import com.interview.weatherapp.data.dto.WeatherDTO;
import com.interview.weatherapp.utils.Constants;

public class WeatherAdapter extends ListAdapter<WeatherDTO, WeatherAdapter.WeatherViewHolder> {

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
        WeatherDTO weatherDTO = getItem(position);
        holder.cityName.setText(weatherDTO.getCityName());
        String tempString = weatherDTO.getTemperature() + "\u00B0 C";
        holder.temperature.setText(tempString);
        String desc = weatherDTO.getDescription();
        holder.description.setText(desc);
        long timeInMillis = weatherDTO.getLastUpdatedTime();
        CharSequence timeAgo = android.text.format.DateUtils.getRelativeTimeSpanString(
                timeInMillis,
                System.currentTimeMillis(),
                android.text.format.DateUtils.MINUTE_IN_MILLIS
        );
        holder.lastUpdatedTime.setText("Updated: " + timeAgo.toString());
        String iconCode = weatherDTO.getIconCode();
        String imageUrl = Constants.IMAGE_URL + iconCode + "@2x.png";
        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(holder.ivWeatherIcon);

    }

    static class WeatherDiffCallback extends DiffUtil.ItemCallback<WeatherDTO> {
        @Override
        public boolean areItemsTheSame(@NonNull WeatherDTO oldItem, @NonNull WeatherDTO newItem) {
            return oldItem.getCityName().equals(newItem.getCityName());
        }

        @Override
        public boolean areContentsTheSame(@NonNull WeatherDTO oldItem, @NonNull WeatherDTO newItem) {
            return oldItem.getTemperature() == newItem.getTemperature();
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
