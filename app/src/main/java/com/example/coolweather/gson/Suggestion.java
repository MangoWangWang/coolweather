package com.example.coolweather.gson;

import android.support.annotation.StringRes;

import com.google.gson.annotations.SerializedName;

/**
 * Created by MangoWang on 2017/9/20.
 */

// 一些建议
public class Suggestion {

    @SerializedName("comf")
    public Comfort comfort;  // 舒适度类

    @SerializedName("cw")
    public CarWash carWash; // 洗车建议类

    @SerializedName("sport")
    public Sport sport; // 运动建议

    public class Comfort
    {
        @SerializedName("txt")
        public String info; // 舒适度提示
    }


    public class CarWash
    {
        @SerializedName("txt")
        public String info; // 洗车提示
    }


    public class Sport
    {
        @SerializedName("txt")
        public String info; // 运动提示
    }


}
