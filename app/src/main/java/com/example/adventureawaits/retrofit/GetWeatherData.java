package com.example.adventureawaits.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetWeatherData {
    @GET("weather")
    Call<OpenWeatherApiResponse> getWeather(@Query("q") String city, @Query("appid") String apiId);
}
