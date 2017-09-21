package com.example.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by MangoWang on 2017/9/19.
 */

public class County extends DataSupport {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    // 县iD
    private int id;
    // 县名字
    private String countyName;
    // 县的天气标识(用于获取详细的天气信息)
    private String weatherId;
    // 城市Id
    private int cityId;
}
