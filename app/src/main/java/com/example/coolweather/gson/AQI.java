package com.example.coolweather.gson;

/**
 * Created by MangoWang on 2017/9/20.
 */



public class AQI {
    public AQICity city; // 城市空气总质量

    public class AQICity
    {
        public String aqi;  // 空气质量
        public String pm25; // PM2.5的值
    }
}
