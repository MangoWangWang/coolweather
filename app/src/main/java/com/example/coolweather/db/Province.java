package com.example.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by MangoWang on 2017/9/19.
 */

public class Province  extends DataSupport{
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }

    // 省ID
    private int id;

    // 省名字
    private String provinceName;

    // 省代号
    private int provinceCode;


}
