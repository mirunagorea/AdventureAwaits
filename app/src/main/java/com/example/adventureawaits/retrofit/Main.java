package com.example.adventureawaits.retrofit;

import com.google.gson.annotations.SerializedName;

public class Main {
//         "main": {
//        "temp": 280.32,
//                "pressure": 1012,
//                "humidity": 81,
//                "temp_min": 279.15,
//                "temp_max": 281.15
//    }

    @SerializedName("temp")
    private Double temperature;

    @SerializedName("humidity")
    private String humidity;

    @SerializedName("temp_min")
    private Double minTemperature;

    @SerializedName("temp_max")
    private Double maxTemperature;

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public Double getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(Double minTemperature) {
        this.minTemperature = minTemperature;
    }

    public Double getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(Double maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    @Override
    public String toString() {
        return "Main{" +
                "temperature='" + temperature + '\'' +
                ", humidity='" + humidity + '\'' +
                ", minTemperature='" + minTemperature + '\'' +
                ", maxTemperature='" + maxTemperature + '\'' +
                '}';
    }
}
