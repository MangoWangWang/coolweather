package com.example.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by MangoWang on 2017/9/20.
 */

// 天气预报
public class Forecast {

    public String date; // 日期

    @SerializedName("tmp")
    public Temperature temperature; // 温度类

    @SerializedName("cond")
    public More more; // 更多信息内部类

    public class Temperature {
        public String max;  // 预测最高温度

        public String min;  // 预测最低温度
    }

    public class More
    {
        @SerializedName("txt_d")
        public String info;  // 预报天气信息
    }


}
