package com.example.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by MangoWang on 2017/9/19.
 */

public class City extends DataSupport {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    // 城市Id
    private int id;

    // 城市名字
    private String cityName;

    // 城市代号
    private int cityCode;

    // 省Id
    private int provinceId;

}
