package com.example.coolweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by MangoWang on 2017/9/20.
 */

/**
 * 总的实例来引用刚刚利用JSON数据创建的实体类
 */
public class Weather {

    public String status; // 返回信息状态(ok表示成功)

    public Basic basic; // 基础信息

    public AQI aqi; // 空气质量

    public Now now;  // 实时状况

    public Suggestion suggestion; // 一些建议

    @SerializedName("daily_forecast")
    public List<Forecast> forecastList; // 天气预报(未来三天)
}
