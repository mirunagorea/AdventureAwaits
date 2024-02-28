package com.example.adventureawaits.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Sys {
    //     "sys": {
//        "type": 1,
//                "id": 5091,
//                "message": 0.0103,
//                "country": "GB",
//                "sunrise": 1485762037,
//                "sunset": 1485794875
//    }
    @SerializedName("sunrise")
    private Integer sunrise;

    @SerializedName("sunset")
    private Integer sunset;

    @SerializedName("country")
    private String country;

    public Integer getSunrise() {
        return sunrise;
    }

    public void setSunrise(Integer sunrise) {
        this.sunrise = sunrise;
    }

    public Integer getSunset() {
        return sunset;
    }

    public void setSunset(Integer sunset) {
        this.sunset = sunset;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "Sys{" +
                "sunrise=" + sunrise +
                ", sunset=" + sunset +
                ", country='" + country + '\'' +
                '}';
    }
}
