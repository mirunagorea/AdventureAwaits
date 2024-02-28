package com.example.adventureawaits.retrofit;

import com.google.gson.annotations.SerializedName;

public class Wind {
//         "wind": {
//        "speed": 4.1,
//                "deg": 80
//    }
    @SerializedName("speed")
    private Float windSpeed;

    public Float getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(Float windSpeed) {
        this.windSpeed = windSpeed;
    }

    @Override
    public String toString() {
        return "Wind{" +
                "windSpeed='" + windSpeed + '\'' +
                '}';
    }
}
