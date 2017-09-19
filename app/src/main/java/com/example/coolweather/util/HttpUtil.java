package com.example.coolweather.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by MangoWang on 2017/9/19.
 */

public class HttpUtil  {
    public static void sendOkHttpRequest(String address,okhttp3.Callback callback)
    {
        // 创建一个客户端
        OkHttpClient client = new OkHttpClient();
        // 创建一个请求
        Request request = new Request.Builder().url(address).build();
        // 通过Url地址发送请求
        client.newCall(request).enqueue(callback);
    }
}
