package com.example.coolweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ScrollingView;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.coolweather.gson.Forecast;
import com.example.coolweather.gson.Weather;
import com.example.coolweather.service.AutoUpdateService;
import com.example.coolweather.util.HttpUtil;
import com.example.coolweather.util.Utility;

import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    private ScrollView weatherLayout; // 天气布局

    private TextView titleCity; // 城市标题

    private TextView titleUpdateTime; // 更新时间

    private TextView degreeText; // 实时温度

    private TextView weatherInfoText; // 天气消息

    private LinearLayout forecastLayout;  // 预测布局

    private TextView aqiText;  // 空气程度

    private TextView pm25Text; // pm2.5程度

    private TextView comfortText; // 舒适度

    private TextView carWashText; // 洗车建议

    private TextView sportText; // 运用建议

    private ImageView bingPicImg; // 每日背景图片(必应)

    public SwipeRefreshLayout swipeRefresh; // 下拉刷新控件

    public DrawerLayout drawerLayout;  // 侧滑面版

    private Button navButton; // 导航按钮


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 版本号大于或等于21时候生效
        // 将背景融入系统状态栏(即最上的)
        if(Build.VERSION.SDK_INT>=21)
        {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            // 讲状态栏变成透明色
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        setContentView(R.layout.activity_weather);

        // 处理侧滑事件
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navButton = (Button) findViewById(R.id.nav_button);

        // 以开始方式打开侧滑面板
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        // 初始化各控件
        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        titleCity = (TextView) findViewById(R.id.title_city);
        titleUpdateTime = (TextView) findViewById(R.id.title_update_time);
        degreeText = (TextView) findViewById(R.id.degree_text);
        weatherInfoText = (TextView) findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
        aqiText = (TextView) findViewById(R.id.aqi_text);
        pm25Text = (TextView) findViewById(R.id.pm25_text);
        comfortText = (TextView) findViewById(R.id.comfort_text);
        carWashText = (TextView) findViewById(R.id.car_wash_text);
        sportText = (TextView) findViewById(R.id.sport_text);

        // 背景图片
        bingPicImg = (ImageView) findViewById(R.id.bing_pic_img);

        // 下拉刷新
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        // 设置下拉进度条的颜色
        swipeRefresh.setColorSchemeColors(R.color.colorPrimary);




        /**
         * 先从缓存中寻找数据,没有的话再从网络上获取
         */
        // 用于存储数据
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather", null);

        final String weatherId;

        if(weatherString != null)
        {
            // 有缓存时直接解析天气数据
            Weather weather = Utility.handleWeatherResponse(weatherString);
            weatherId = weather.basic.weatherId;
            showWeatherInfo(weather);
        }else
        {
            // 无缓存时去服务器查询天气
            weatherId = getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE); // 不可见,但暂用位置
            requestWeather(weatherId);
        }

        /**
         * 下拉刷新天气
         *
         */
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(weatherId);
            }
        });



        /**
         * 先从缓存中寻找背景图片,没有的话从网络上加载
         */
        // 存在图片缓存的话返回图片,没有的话返回null
        String bingPic = prefs.getString("bing_pic",null);
        if(bingPic != null)
        {
            // 裁剪图片并放到相应控件中
            Glide.with(this).load(bingPic).into(bingPicImg);
        }else
        {
            loadBingPic();
        }
    }

    /**
     * 根据天气ID去查询天气信息
     */
    public void requestWeather(final String weatherId)
    {
        // 请求天气字符串
        String weatherUrl = "http://guolin.tech/api/weather?cityid=" + weatherId+"&key=0a08e3292b144910bf0e6742b7465222";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        // 表示刷新请求结束,并隐藏进度条
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            // 返回成功后处理方法
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather !=null && "ok".equals(weather.status)) // 判定返回状态
                        {
                            // 获取默认的preferenceManager
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather",responseText);
                            // 写入preference文件中
                            editor.apply();
                            showWeatherInfo(weather);
                        }else
                        {
                            Toast.makeText(WeatherActivity.this, "获得天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                        // 表示刷新请求结束,并隐藏进度条
                        swipeRefresh.setRefreshing(false);
                    }
                });

            }
        });
        loadBingPic();
    }

    private void showWeatherInfo(Weather weather)
    {
        // 获取有关的数据
        String cityName = weather.basic.cityName;
        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        String degree = weather.now.temperature + "℃";
        String weatherInfo = weather.now.more.info;
        // 讲数据展示在界面上
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);

        forecastLayout.removeAllViews();
        // 动态添加温馨提示
        for (Forecast forecast : weather.forecastList)
        {
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item,forecastLayout,false);
            TextView dateText = (TextView) view.findViewById(R.id.date_text);
            TextView infoText = (TextView) view.findViewById(R.id.info_text);
            TextView maxText = (TextView) view.findViewById(R.id.max_text);
            TextView minText = (TextView) view.findViewById(R.id.min_text);
            dateText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);
            // 将View添加到预测天气布局上
            forecastLayout.addView(view);
        }
        if (weather.aqi !=null)
        {
            aqiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
        }

        String comfort = "舒适度:"+weather.suggestion.comfort.info;
        String carWash = "洗车指数:" + weather.suggestion.carWash.info;
        String sport = "运动建议:" + weather.suggestion.sport.info;
        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);

        weatherLayout.setVisibility(View.VISIBLE);

        // 成功更新后在后台运行,每八小时更新一次天气
        Intent intent = new Intent(this, AutoUpdateService.class);
        startService(intent);



    }

    /**
     * 加载每日必应的每日一图
     */
    private void loadBingPic()
    {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_pic",bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
                    }
                });

            }
        });
    }
}


















