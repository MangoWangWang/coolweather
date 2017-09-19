package com.example.coolweather.gson;

/**
 * Created by MangoWang on 2017/9/20.
 */

public class AQI {
    public AQICity city;

    public class AQICity
    {
        public String aqi;
        public String pm25;
    }
}
