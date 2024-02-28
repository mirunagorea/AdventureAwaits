package com.example.adventureawaits.retrofit;

public interface OnGetWeatherCallback {
    void onSuccess(OpenWeatherApiResponse weather);

    void onError();
}
