package com.example.colorfulweather.WeatherInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.example.colorfulweather.R;
import com.example.colorfulweather.Resource.CityBean;
import com.example.colorfulweather.Resource.InfoBean;
import com.example.colorfulweather.Resource.InternetResource;
import com.example.colorfulweather.Resource.MyDataBaseHelper;
import com.example.colorfulweather.Resource.WeatherBean;
import com.example.colorfulweather.WeatherAir.AirContextActivity;
import com.example.colorfulweather.WeatherArea.ChooseAreaActivity;
import com.example.colorfulweather.WeatherDays.DaysContextActivity;
import com.example.colorfulweather.WeatherMore.MoreLayout;
import com.example.colorfulweather.WeatherMore.WeatherMoreItemLayout;
import com.example.colorfulweather.WeatherMsg.MsgLayout;
import com.example.colorfulweather.WeatherRainfall.RainfallActivity;
import com.example.colorfulweather.WeatherSettings.MyService;
import com.example.colorfulweather.WeatherSettings.SettingsActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.permissionx.guolindev.PermissionX;
import com.qweather.sdk.bean.IndicesBean;
import com.qweather.sdk.bean.base.IndicesType;
import com.qweather.sdk.bean.base.Lang;
import com.qweather.sdk.bean.weather.WeatherDailyBean;
import com.qweather.sdk.bean.weather.WeatherHourlyBean;
import com.qweather.sdk.bean.weather.WeatherNowBean;
import com.qweather.sdk.view.HeConfig;
import com.qweather.sdk.view.QWeather;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CHOOSE_AREA = 0;
    private static final int NO_INTERNET = 1;
    private static final int FAIL_LOCATION = 2;
    private ActionBar actionBar;
    private InfoLayout infoLayout;
    private MoreLayout moreLayout;
    private MsgLayout msgLayout;
    private InfoBean infoBean;
    private TextView address;
    private CityBean cityBean;
    private AMapLocationClient client;
    private MyHandler handler = new MyHandler();
    private List<WeatherBean> weatherBeans = new ArrayList<>();
    private MyDataBaseHelper helper = new MyDataBaseHelper(this, "data", null, 1);

    class MyHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case NO_INTERNET:
                    Toast.makeText(MainActivity.this, "当前无网络", Toast.LENGTH_SHORT).show();
                    break;
                case FAIL_LOCATION:
                    Toast.makeText(MainActivity.this, "定位失败", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    }

    interface MyListener{
        void savedMsg(WeatherBean weatherBean);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        ADFragment fragment = new ADFragment();
        transaction.add(R.id.fragment, fragment);
        transaction.commit();
        PermissionX.init(this)
        .permissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS)
        .explainReasonBeforeRequest()
        .onExplainRequestReason((scope, deniedList, beforeRequest) -> scope.showRequestReasonDialog(deniedList, "即将向您申请以下权限", "好的", "退出程序"))
        .onForwardToSettings((scope, deniedList) -> scope.showForwardToSettingsDialog(deniedList, "您可能需要手动开启以下权限", "好的", "退出程序"))
        .request((allGranted, grantedList, deniedList) -> {
            if (allGranted) {
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if(networkInfo == null || !networkInfo.isConnected()){
                    AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                        .setTitle("无网络")
                        .setMessage("0202年了，不会还有人不联网吧")
                        .setCancelable(false)
                        .setNegativeButton("退出程序", (dialog1, which) -> {
                            finish();
                        });
                    dialog.show();
                    return;
                }
                LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                    AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                            .setTitle("无法确定您的位置")
                            .setMessage("Android 9以上可能需要您打开位置信息才能获取位置")
                            .setCancelable(false)
                            .setNegativeButton("退出程序", (dialog1, which) -> {
                                finish();
                            });
                    dialog.show();
                    return;
                }
                new Handler().postDelayed(() -> {
                    FragmentTransaction t = manager.beginTransaction();
                    t.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                    t.remove(fragment);
                    t.commitAllowingStateLoss();
                }, 1500);
                Toolbar toolbar = findViewById(R.id.toolBar);
                toolbar.setTitle("");
                setSupportActionBar(toolbar);
                actionBar = getSupportActionBar();
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeAsUpIndicator(R.drawable.gps);
                address = findViewById(R.id.detail_toolbar_name);
                infoLayout = findViewById(R.id.weatherInfo);
                moreLayout = findViewById(R.id.weatherMore);
                moreLayout.setMyOnClickListener(v -> {
                    Intent intent = null;
                    switch (v.getId()){
                        case R.id.btn:
                            intent = new Intent(MainActivity.this, DaysContextActivity.class);
                            break;
                        case R.id.air:
                            intent = new Intent(MainActivity.this, AirContextActivity.class);
                            break;
                        case R.id.drop:
                            intent = new Intent(MainActivity.this, RainfallActivity.class);
                            break;
                        default:
                            break;
                    }
                    intent.putExtra("city", cityBean);
                    startActivity(intent);
                });
                msgLayout = findViewById(R.id.weatherMsg);
                FloatingActionButton actionButton = findViewById(R.id.floatActionButton);
                actionButton.setOnClickListener(v -> {
                    Intent intent = new Intent(this, ChooseAreaActivity.class);
                    startActivityForResult(intent, REQUEST_CHOOSE_AREA);
                });
                HeConfig.init(
                        "key",
                        "id"
                );
                HeConfig.switchToDevService();
                getLocation();
            } else {
                finish();
            }
        });
    }

    private void getLocation() {
        client = new AMapLocationClient(getApplicationContext());
        client.setLocationListener(aMapLocation -> {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    String county = aMapLocation.getDistrict();
                    String city = aMapLocation.getCity();
                    InternetResource.sendHttpRequest(ChooseAreaActivity.CITY_SERVICE + county + "&adm2=" +city, new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            handler.sendEmptyMessage(NO_INTERNET);
                        }
                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            if(!response.isSuccessful()) {
                                onFailure(call, new IOException());
                                return;
                            }
                            try {
                                JSONObject rootObj = new JSONObject(response.body().string());
                                if(rootObj.getString("code").equals("200")){
                                    JSONArray rootArr = rootObj.getJSONArray("location");
                                    JSONObject object = rootArr.getJSONObject(0);
                                    cityBean = new CityBean(object.getString("id"), object.getString("country"), object.getString("adm1"), object.getString("adm2"), object.getString("name"));
                                    if(getSharedPreferences("settings", MODE_PRIVATE).getBoolean("showNotification", true)){
                                        Intent intent = new Intent(MainActivity.this, MyService.class);
                                        intent.putExtra("data", cityBean);
                                        startService(intent);
                                    }
                                    updateFromServer(cityBean, null);
                                    return;
                                }
                            } catch (JSONException e) {
                            }
                            onFailure(call, new IOException());
                        }
                    });
                    client.stopLocation();
                    return;
                }
            }
            handler.sendEmptyMessage(FAIL_LOCATION);
        });
        AMapLocationClientOption option = new AMapLocationClientOption();
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        option.setOnceLocation(true);
        if(client != null){
            client.setLocationOption(option);
            client.stopLocation();
            client.startLocation();
        }
        client.startLocation();
    }

    private void updateFromServer(CityBean cityBean, MyListener listener){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo == null || !networkInfo.isConnected()){
            handler.sendEmptyMessage(NO_INTERNET);
            return;
        }
        this.cityBean = cityBean;
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
                    weatherBeans.clear();
                    for (int i = 0; i < dailyBeans.size(); i++) {
                        WeatherDailyBean.DailyBean dailyBean = dailyBeans.get(i);
                        weatherBeans.add(new WeatherBean(type[i], dailyBean.getTempMax(), dailyBean.getTempMin(), dailyBean.getTextDay(), dailyBean.getTextNight()));
                    }
                    moreLayout.updateDays(weatherBeans);
                    if(listener != null){
                        listener.savedMsg(weatherBeans.get(0));
                    }
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
                        updateFromServer(cityBean, weatherBean -> {
                            SQLiteDatabase db = helper.getWritableDatabase();
                            Cursor cursor = db.rawQuery("select * from city where id = ?", new String[]{cityBean.getId()});
                            if(cursor.getCount() < 1) {
                                db.execSQL("insert into city (id, city, county) values (?, ?, ?)", new String[]{cityBean.getId(), cityBean.getCity(), cityBean.getCounty()});
                            }
                            db.execSQL("update city set textDay = ?, textNight = ?, tempMax = ?, tempMin = ? where id = ?",
                                    new String[]{weatherBean.getTextDay(), weatherBean.getTextNight(), weatherBean.getTempMax(), weatherBean.getTempMin(), cityBean.getId()});
                        });
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
                updateFromServer(cityBean, null);
                break;
            case R.id.settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                intent.putExtra("data", cityBean);
                startActivity(intent);
                break;
            case android.R.id.home:
                getLocation();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(client != null){
            client.onDestroy();
        }
    }
}
