package com.example.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by MangoWang on 2017/9/20.
 */

// 实时天气状况
public class Now {

    @SerializedName("tmp")
    public String temperature;  // 实时温度

    @SerializedName("cond")
    public More more; // 实时更多

    public class More
    {
        @SerializedName("txt")
        public String info; // 实时天气概况
    }
}
