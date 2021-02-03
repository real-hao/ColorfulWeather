package com.example.colorfulweather.WeatherDays;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;

import com.example.colorfulweather.R;
import com.example.colorfulweather.Resource.CityBean;
import com.example.colorfulweather.Resource.DailyWeather;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.qweather.sdk.bean.weather.WeatherDailyBean;
import com.qweather.sdk.view.QWeather;

import java.util.ArrayList;
import java.util.List;

public class DaysContextActivity extends AppCompatActivity {
    private static final int UPDATE_UI = 0;
    private RecyclerView recyclerView;
    private DaysContextAdapter adapter;
    private FloatingActionButton actionButton;
    private List<DailyWeather> dailyWeatherList;
    private MyHandler handler = new MyHandler();

    class MyHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case UPDATE_UI:
                    adapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_days_context);
        Toolbar toolbar = findViewById(R.id.day_toolBar);
        toolbar.setTitle("近15日天气");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        recyclerView = findViewById(R.id.daysView);
        actionButton = findViewById(R.id.goBack);
        actionButton.setOnClickListener(v -> {
            finish();
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        dailyWeatherList = new ArrayList<>();
        adapter = new DaysContextAdapter(dailyWeatherList);
        recyclerView.setAdapter(adapter);
        CityBean cityBean = (CityBean) getIntent().getSerializableExtra("city");
        QWeather.getWeather15D(this, cityBean.getId(), new QWeather.OnResultWeatherDailyListener() {
            @Override
            public void onError(Throwable throwable) {
            }
            @Override
            public void onSuccess(WeatherDailyBean weatherDailyBean) {
                if(weatherDailyBean.getCode().equals("200")){
                    dailyWeatherList.clear();
                    List<WeatherDailyBean.DailyBean> dailyBeans = weatherDailyBean.getDaily();
                    for (int i = 0; i < dailyBeans.size(); i++) {
                        WeatherDailyBean.DailyBean dailyBean = dailyBeans.get(i);
                        dailyWeatherList.add(new DailyWeather(dailyBean.getFxDate(), dailyBean.getTempMax(), dailyBean.getTempMin(), dailyBean.getTextDay(), dailyBean.getTextNight(), dailyBean.getWindDirDay(), dailyBean.getWindScaleDay(), dailyBean.getWindScaleNight(), dailyBean.getHumidity()));
                    }
                    handler.sendEmptyMessage(UPDATE_UI);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}