package com.example.adventureawaits.retrofit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OpenWeatherApiRepository {
    protected static final String BASE_WEATHER_URL = "https://api.openweathermap.org/data/2.5/";
    private static OpenWeatherApiRepository openWeatherApiRepository;
    private GetWeatherData getWeatherData;

    private OpenWeatherApiRepository(GetWeatherData api) {
        this.getWeatherData = api;
    }

    public static OpenWeatherApiRepository getInstance() {
        if (openWeatherApiRepository == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_WEATHER_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            openWeatherApiRepository = new OpenWeatherApiRepository(retrofit.create(GetWeatherData.class));
        }
        return openWeatherApiRepository;
    }

    public void getWeather(final OnGetWeatherCallback callback, String city, String apiId) {
        getWeatherData.getWeather(city, apiId)
                .enqueue(new Callback<OpenWeatherApiResponse>() {
                    @Override
                    public void onResponse(Call<OpenWeatherApiResponse> call, Response<OpenWeatherApiResponse> response) {
                        if (response.isSuccessful()) {
                            OpenWeatherApiResponse weather = response.body();
                            if (weather != null) {
                                callback.onSuccess(weather);
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<OpenWeatherApiResponse> call, Throwable t) {
                        callback.onError();
                    }
                });
    }
}
