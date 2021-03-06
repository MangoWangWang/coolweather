package com.example.coolweather;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.wifi.aware.PublishConfig;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coolweather.db.City;
import com.example.coolweather.db.County;
import com.example.coolweather.db.Province;
import com.example.coolweather.util.HttpUtil;
import com.example.coolweather.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by MangoWang on 2017/9/19.
 * 用于加载数据的fragment
 */


public class ChooseAreaFragment extends android.support.v4.app.Fragment { // 继承自向下兼容的扩展包

    public static final int LEVEL_PROVINCE = 0;  // 等级:省

    public static final int LEVEL_CITY = 1;  // 等级:市

    public static final int LEVEL_COUNTY = 2;  // 等级:县

    public ProgressDialog progressDialog;  // 进度框

    private TextView titleText; // 标题文本框

    private Button backButton; // 返回上一级按钮

    private ListView listView; // 显示信息列表(即省,市,县)

    private ArrayAdapter<String> adapter;  // 数组适配器(用于适配ListView)

    private List<String> dataList = new ArrayList<>();  // 数据列表

    /**
     * 省列表
     */
    private  List<Province> provinceList;


    /**
     * 市列表
     */
    private  List<City> cityList;

    /**
     * 县列表
     */
    private  List<County> countyList;

    /**
     * 选中的省
     */
    private Province selectedProvince;

    /**
     * 选中的市
     */
    private City selectedCity;


    /**
     * 当前选中的级别
     */
    private int currentLevel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area,container,false); // 加载choose_area的布局文件
        // 实例化空间
        titleText = (TextView)view.findViewById(R.id.title_text);
        backButton = (Button) view.findViewById(R.id.back_button);
        listView = (ListView) view.findViewById(R.id.list_view);

        // 创建适配器,指定数据源和item样式
        adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,dataList);
        // 设置适配器
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 设置item的监听事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (currentLevel == LEVEL_PROVINCE)
                {
                    selectedProvince = provinceList.get(position); // 获取选中的省份
                    queryCities();

                }else if (currentLevel == LEVEL_CITY)
                {
                    selectedCity = cityList.get(position);  // 获取选中的市
                    queryCounties();
                }else if (currentLevel == LEVEL_COUNTY)
                {
                    String weatherId = countyList.get(position).getWeatherId();  // 获取weatherId


                    // 分别在不同活动选中县城所执行的不同情况
                    if (getActivity() instanceof MainActivity) // 在主界面的情况
                    {
                        Intent intent = new Intent(getActivity(),WeatherActivity.class);
                        intent.putExtra("weather_id",weatherId);
                        startActivity(intent);
                        getActivity().finish();
                    }else if (getActivity() instanceof WeatherActivity) // 在天气活动的情况
                    {
                        WeatherActivity activity = (WeatherActivity)getActivity();
                        activity.drawerLayout.closeDrawer(GravityCompat.START);  // 关闭侧滑activity,必须给定一个方向
                        activity.swipeRefresh.setRefreshing(true); // 启动下拉刷新
                        activity.requestWeather(weatherId);
                    }



                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // button的点击事件
                if (currentLevel == LEVEL_COUNTY)
                {
                    queryCities();
                }else if (currentLevel == LEVEL_CITY)
                {
                    queryProvinces();
                }
            }
        });
        queryProvinces();
    }

    // 请求省的数据
    private void queryProvinces()
    {
        titleText.setText("China");
        backButton.setVisibility(View.GONE); // 不可见,不占用地方
        provinceList = DataSupport.findAll(Province.class);  // 能获取到Province类型的数据集合
        if (provinceList.size()>0)
        {
            for (Province province : provinceList) // 遍历循环添加数据
            {
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged(); // 通知适配器刷新界面
            listView.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
        }else  // 再一次请求所有省的信息
        {
            String address = "http://guolin.tech/api/china"; // 发出请求
            queryFromServer(address,"province");
        }
    }

    // 请求市的数据
    private void queryCities()
    {
        titleText.setText(selectedProvince.getProvinceName());
        backButton.setVisibility(View.VISIBLE);
        cityList = DataSupport.where("provinceid = ?",String.valueOf(selectedProvince.getId())).find(City.class);

        if (cityList.size()>0)
        {
            dataList.clear();
            for (City city : cityList)
            {
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_CITY;
        }else
        {
            int provinceCode = selectedProvince.getProvinceCode();
            String address = "http://guolin.tech/api/china/"+provinceCode;
            queryFromServer(address,"city");
        }
    }


    private void queryCounties()
    {
        titleText.setText(selectedCity.getCityName());
        backButton.setVisibility(View.VISIBLE);
        // 取出特定城市号的县的集合
        countyList = DataSupport.where("cityid = ?",String.valueOf(selectedCity.getId())).find(County.class);

        if (countyList.size()>0)
        {
            dataList.clear(); // 清空dataList
            for (County county : countyList)
            {
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_COUNTY;
        }else
        {
            int provinceCode = selectedProvince.getProvinceCode();
            int cityCode = selectedCity.getCityCode();
            String address = "http://guolin.tech/api/china/"+provinceCode+"/"+cityCode;
            queryFromServer(address,"county");
        }
    }


    private void queryFromServer(String address,final String type)
    {
        showProgressDialog();
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                // 通过runOnUiThread()方法回到主线程处理逻辑
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog(); // 关闭加载进度条
                        Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String responseText = response.body().string();
                boolean result = false;

                // 根据不同的级别处理信息
                if ("province".equals(type))
                {
                    result = Utility.handleProvincesResponse(responseText); // 处理省
                }else if ("city".equals(type))
                {
                    result = Utility.handleCitiesResponse(responseText,selectedProvince.getId()); // 处理市
                }else if ("county".equals(type))
                {
                    result = Utility.handleCountiesResponse(responseText,selectedCity.getId()); // 处理县
                }
                if (result)
                {
                    // 查询完后进行UI界面的刷新
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(type))
                            {
                                queryProvinces();
                            }else if ("city".equals(type))
                            {
                                queryCities();
                            }else if ("county".equals(type))
                            {
                                queryCounties();
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * 显示进度对话框
     *
     */
    private void showProgressDialog()
    {
        if (progressDialog == null)
        {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    /**
     * 关闭进度对话框
     */
    private void closeProgressDialog()
    {
        if (progressDialog!=null)
        {
            progressDialog.dismiss();
        }
    }
}













