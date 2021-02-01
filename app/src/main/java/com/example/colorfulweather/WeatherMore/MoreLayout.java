package com.example.colorfulweather.WeatherMore;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.colorfulweather.R;
import com.example.colorfulweather.Resource.WeatherBean;
import com.example.colorfulweather.Resource.WeatherHourly;
import com.example.colorfulweather.WeatherAir.AirContextActivity;
import com.example.colorfulweather.WeatherDays.DaysContextActivity;
import com.example.colorfulweather.WeatherRainfall.RainfallActivity;
import com.qweather.sdk.bean.weather.WeatherHourlyBean;

import java.util.ArrayList;
import java.util.List;

public class MoreLayout extends LinearLayout {
    private static final int UPDATE_UI = 0;
    private TextView air;
    private TextView drop;
    private WeatherMoreItemLayout[] days;
    private RecyclerView recyclerView;
    private List<WeatherHourly> weatherHourlies;
    private WeatherHourlyAdapter adapter;
    private MoreHandler handler = new MoreHandler();

    class MoreHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case UPDATE_UI:
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    public MoreLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.weather_more_layout, this);
        Button button = findViewById(R.id.btn);
        button.setOnClickListener(v -> {
            getContext().startActivity(new Intent(getContext(), DaysContextActivity.class));
        });
        air = findViewById(R.id.air);
        air.setOnClickListener(v -> {
            getContext().startActivity(new Intent(getContext(), AirContextActivity.class));
        });
        drop = findViewById(R.id.drop);
        drop.setOnClickListener(v -> {
            getContext().startActivity(new Intent(getContext(), RainfallActivity.class));
        });
        days = new WeatherMoreItemLayout[] {findViewById(R.id.today), findViewById(R.id.tom), findViewById(R.id.atom), };
        recyclerView = findViewById(R.id.hourly);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(manager);
        weatherHourlies = new ArrayList<>();
        adapter = new WeatherHourlyAdapter(weatherHourlies);
        recyclerView.setAdapter(adapter);
    }

    public void updateDays(List<WeatherBean> weatherList){
        for (int i = 0; i < 3; i ++) {
            WeatherBean weatherBean = weatherList.get(i);
            days[i].update(weatherBean);
        }
    }

    public void updateHourly(List<WeatherHourlyBean.HourlyBean> list){
        weatherHourlies.clear();
        for (int i = 0; i < list.size(); i ++){
            WeatherHourlyBean.HourlyBean hourlyBean = list.get(i);
            String time = hourlyBean.getFxTime();
            weatherHourlies.add(new WeatherHourly(time.substring(time.indexOf('T')+1, time.indexOf('T')+6), hourlyBean.getTemp(), hourlyBean.getText(), hourlyBean.getWindScale(), hourlyBean.getWindDir()));
        }
        handler.sendMessage(handler.obtainMessage(UPDATE_UI));
    }
}
