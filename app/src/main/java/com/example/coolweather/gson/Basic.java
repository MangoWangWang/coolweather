package com.example.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by MangoWang on 2017/9/20.
 */

public class Basic  {

    @SerializedName("city")  // 用于gson名字的对应
    public String cityName; // 城市名字

    @SerializedName("id")
    public String weatherId;  // 天气标识符

    public Update update;   // 更新时间类

    public class Update
    {
        @SerializedName("loc")
        public String updateTime; // 更新时间
    }


}
