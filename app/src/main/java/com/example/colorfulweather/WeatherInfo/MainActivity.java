package com.example.colorfulweather.WeatherInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.colorfulweather.R;
import com.example.colorfulweather.Resource.CityBean;
import com.example.colorfulweather.Resource.InfoBean;
import com.example.colorfulweather.Resource.WeatherBean;
import com.example.colorfulweather.WeatherArea.ChooseAreaActivity;
import com.example.colorfulweather.WeatherMore.MoreLayout;
import com.example.colorfulweather.WeatherMore.WeatherMoreItemLayout;
import com.example.colorfulweather.WeatherMsg.MsgLayout;
import com.example.colorfulweather.WeatherSettings.SettingsActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.qweather.sdk.bean.IndicesBean;
import com.qweather.sdk.bean.base.IndicesType;
import com.qweather.sdk.bean.base.Lang;
import com.qweather.sdk.bean.weather.WeatherDailyBean;
import com.qweather.sdk.bean.weather.WeatherHourlyBean;
import com.qweather.sdk.bean.weather.WeatherNowBean;
import com.qweather.sdk.view.HeConfig;
import com.qweather.sdk.view.QWeather;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CHOOSE_AREA = 0;
    private ActionBar actionBar;
    private InfoLayout infoLayout;
    private MoreLayout moreLayout;
    private MsgLayout msgLayout;
    private InfoBean infoBean;
    private TextView address;
    private List<WeatherBean> weatherBeans = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.gps);
        address = findViewById(R.id.detail_toolbar_name);
        infoLayout = findViewById(R.id.weatherInfo);
        moreLayout = findViewById(R.id.weatherMore);
        msgLayout = findViewById(R.id.weatherMsg);
        FloatingActionButton actionButton = findViewById(R.id.floatActionButton);
        actionButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, ChooseAreaActivity.class);
            startActivityForResult(intent, REQUEST_CHOOSE_AREA);
        });
        HeConfig.init(
                "HE2101261647231428",
                "9259105ba23f4f8ab7619f3fc8c9ef14"
        );
        HeConfig.switchToDevService();
        updateFromServer(new CityBean("101010100", "", "", "北京", "北京"));
    }

    public void updateFromServer(CityBean cityBean){
        runOnUiThread(() -> {
            address.setText(cityBean.getCity().equals(cityBean.getCounty())? cityBean.getCounty() : cityBean.getCity()+"·"+cityBean.getCounty());
        });
        QWeather.getWeatherNow(this, cityBean.getId(), new QWeather.OnResultWeatherNowListener(){
            @Override
            public void onError(Throwable throwable) {
            }
            @Override
            public void onSuccess(WeatherNowBean weatherNowBean) {
                if (weatherNowBean.getCode().equals("200")){
                    WeatherNowBean.NowBaseBean nowBaseBean = weatherNowBean.getNow();
                    infoBean = new InfoBean(nowBaseBean.getTemp(), nowBaseBean.getText());
                    infoLayout.updateData(infoBean);
                    msgLayout.updateDayMsg(nowBaseBean);
                }
            }
        });
        QWeather.getWeather3D(this, cityBean.getId(), new QWeather.OnResultWeatherDailyListener(){
            @Override
            public void onError(Throwable throwable) {
            }
            @Override
            public void onSuccess(WeatherDailyBean weatherDailyBean) {
                if(weatherDailyBean.getCode().equals("200")){
                    List<WeatherDailyBean.DailyBean> dailyBeans = weatherDailyBean.getDaily();
                    int[] type = {WeatherMoreItemLayout.TYPE_TODAY, WeatherMoreItemLayout.TYPE_TOM, WeatherMoreItemLayout.TYPE_ATOM};
                    for (int i = 0; i < dailyBeans.size(); i++) {
                        WeatherDailyBean.DailyBean dailyBean = dailyBeans.get(i);
                        weatherBeans.add(new WeatherBean(type[i], dailyBean.getTextDay().equals(dailyBean.getTextNight())? dailyBean.getTextDay() : dailyBean.getTextDay()+"转"+dailyBean.getTextNight(), dailyBean.getTempMax(), dailyBean.getTempMin()));
                    }
                    moreLayout.updateDays(weatherBeans);
                    msgLayout.updateSunriseAndSunset(new String[]{dailyBeans.get(0).getSunrise(), dailyBeans.get(0).getSunset()});
                }
            }
        });
        QWeather.getWeather24Hourly(this, cityBean.getId(), new QWeather.OnResultWeatherHourlyListener(){
            @Override
            public void onError(Throwable throwable) {
            }
            @Override
            public void onSuccess(WeatherHourlyBean weatherHourlyBean) {
                if(weatherHourlyBean.getCode().equals("200")){
                    List<WeatherHourlyBean.HourlyBean> hourlyBeans = weatherHourlyBean.getHourly();
                    moreLayout.updateHourly(hourlyBeans);
                }
            }
        });
        List<IndicesType> lists = new ArrayList<>();
        lists.add(IndicesType.DRSG);
        lists.add(IndicesType.SPI);
        lists.add(IndicesType.SPT);
        lists.add(IndicesType.CW);
        lists.add(IndicesType.PTFC);
        lists.add(IndicesType.FLU);
        lists.add(IndicesType.COMF);
        lists.add(IndicesType.DC);
        QWeather.getIndices1D(this, cityBean.getId(), Lang.ZH_HANS, lists, new QWeather.OnResultIndicesListener(){
            @Override
            public void onError(Throwable throwable) {
            }
            @Override
            public void onSuccess(IndicesBean indicesBean) {
                if(indicesBean.getCode().equals("200")){
                    msgLayout.updateSuggestion(indicesBean.getDailyList());
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_CHOOSE_AREA:
                if(resultCode == RESULT_OK){
                    if(data != null){
                        CityBean cityBean = (CityBean) data.getSerializableExtra("city");
                        updateFromServer(cityBean);
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.share:
                // ...
                break;
            case R.id.settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            case android.R.id.home:
                // ...
                break;
            default:
                break;
        }
        return true;
    }
}