package com.interview.weatherapp.data.network;

import com.interview.weatherapp.data.network.api.WeatherApi;
import com.interview.weatherapp.utils.Constants;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;
    private static Retrofit getClient() {
        if(retrofit == null) {
            HttpLoggingInterceptor logger = new HttpLoggingInterceptor();
            logger.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logger)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }
        return retrofit;
    }

    public static WeatherApi getApi() {
        return getClient().create(WeatherApi.class);
    }

}
