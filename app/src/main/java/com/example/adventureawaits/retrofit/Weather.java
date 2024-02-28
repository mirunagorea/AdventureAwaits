package com.example.adventureawaits.retrofit;

import com.google.gson.annotations.SerializedName;

public class Weather {
//    {
//        "id": 300,
//            "main": "Drizzle",
//            "description": "light intensity drizzle",
//            "icon": "09d"
//    }
    @SerializedName("main")
    private String shortDescription;

    @SerializedName("description")
    private String longDescription;

    @SerializedName("icon")
    private String icon;

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "shortDescription='" + shortDescription + '\'' +
                ", longDescription='" + longDescription + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }
}
