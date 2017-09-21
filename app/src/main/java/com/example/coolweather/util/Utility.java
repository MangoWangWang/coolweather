package com.example.coolweather.util;

import android.text.TextUtils;

import com.example.coolweather.db.City;
import com.example.coolweather.db.County;
import com.example.coolweather.db.Province;
import com.example.coolweather.gson.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by MangoWang on 2017/9/19.
 */

public class Utility  {
    /*解析和处理服务器返回的省数据*/
    public static boolean handleProvincesResponse(String response)
    {
        if (!TextUtils.isEmpty(response)) // 用于判断返回数据是否为null或数据长度为0
        {
            try {
                /*[{"id":1,"name":"北京"},{"id":2,"name":"上海"},{"id":3,"name":"天津"},{"id":4,"name":"重庆"},
                {"id":5,"name":"香港"},{"id":6,"name":"澳门"},{"id":7,"name":"台湾"},{"id":8,"name":"黑龙江"},
                {"id":9,"name":"吉林"},{"id":10,"name":"辽宁"},{"id":11,"name":"内蒙古"},{"id":12,"name":"河北"},
                {"id":13,"name":"河南"},{"id":14,"name":"山西"},{"id":15,"name":"山东"},{"id":16,"name":"江苏"},
                {"id":17,"name":"浙江"},{"id":18,"name":"福建"},{"id":19,"name":"江西"},{"id":20,"name":"安徽"},
                {"id":21,"name":"湖北"},{"id":22,"name":"湖南"},{"id":23,"name":"广东"},{"id":24,"name":"广西"},
                {"id":25,"name":"海南"},{"id":26,"name":"贵州"},{"id":27,"name":"云南"},{"id":28,"name":"四川"},
                {"id":29,"name":"西藏"},{"id":30,"name":"陕西"},{"id":31,"name":"宁夏"},{"id":32,"name":"甘肃"},
                {"id":33,"name":"青海"},{"id":34,"name":"新疆"}]*/
                JSONArray allProvinces = new JSONArray(response);  // 解析服务器返回的json数组

                // 循环取出里面的json对象
                for (int i = 0; i<allProvinces.length(); i++)
                {
                    JSONObject provinceObject = allProvinces.getJSONObject(i); // 取出json对象
                    Province province = new Province();
                    province.setProvinceName(provinceObject.getString("name")); // 取出内容放到Province类中的属性
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save(); // 利用DataSupport保存到数据库中
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return false;
    }


    /*解析和处理服务器返回的市数据*/
    public static boolean handleCitiesResponse(String response,int provinceId)
    {
        if (!TextUtils.isEmpty(response))
        {
            try {
                JSONArray allCities = new JSONArray(response);
                for (int i = 0; i<allCities.length(); i++)
                {
                    JSONObject cityObject = allCities.getJSONObject(i);
                    City city = new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return false;
    }

    /*解析和处理服务器返回的乡村数据*/
    public static boolean handleCountiesResponse(String response,int cityId)
    {
        if (!TextUtils.isEmpty(response))
        {
            try {
                JSONArray allcounties = new JSONArray(response);
                for (int i = 0; i<allcounties.length(); i++)
                {
                    JSONObject countyObject = allcounties.getJSONObject(i);
                    County county  = new County();
                    county.setCountyName(countyObject.getString("name"));
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return false;
    }

    /**
     * 将返回的JSON数据解析成Weather实体类
     */
    public static Weather handleWeatherResponse(String response)
    {
        try {
            // 解析出一个json对象 { "HeWeather":[]}
            JSONObject jsonObject = new JSONObject(response);
            // 解析出需要的json数组  "HeWeather":[]
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
            // 取出第一个数组
            String weatherContent = jsonArray.getJSONObject(0).toString();
            // 以指定好的模型返回一个势力化的对象
            return new Gson().fromJson(weatherContent,Weather.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


}
