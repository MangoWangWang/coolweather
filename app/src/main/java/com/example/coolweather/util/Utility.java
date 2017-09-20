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
        if (!TextUtils.isEmpty(response))
        {
            try {
                JSONArray allProvinces = new JSONArray(response);
                for (int i = 0; i<allProvinces.length(); i++)
                {
                    JSONObject provinceObject = allProvinces.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save();
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
            // 解析出一个json对象
            JSONObject jsonObject = new JSONObject(response);
            // 解析出需要的json数组
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent,Weather.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


}
