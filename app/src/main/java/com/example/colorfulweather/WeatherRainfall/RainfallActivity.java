package com.example.colorfulweather.WeatherRainfall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.MenuItem;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.Projection;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.example.colorfulweather.R;
import com.example.colorfulweather.Resource.AirInfo;
import com.example.colorfulweather.Resource.CityBean;
import com.example.colorfulweather.Resource.InternetResource;
import com.example.colorfulweather.WeatherAir.AirAdapter;
import com.example.colorfulweather.WeatherAir.AirContextActivity;
import com.example.colorfulweather.WeatherArea.ChooseAreaActivity;
import com.example.colorfulweather.WeatherInfo.MainActivity;
import com.example.colorfulweather.WeatherSettings.MyService;
import com.qweather.sdk.bean.MinutelyBean;
import com.qweather.sdk.bean.air.AirNowBean;
import com.qweather.sdk.bean.base.Code;
import com.qweather.sdk.bean.base.Lang;
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

public class RainfallActivity extends AppCompatActivity {
    private static final int UPDATE_UI = 0;
    private List<AirInfo> infoList;
    private MyHandler handler = new MyHandler();
    private AirAdapter adapter;
    private TextView time;
    private TextView title;
    private MapView mapView;
    private AMap amap;
    private RecyclerView recyclerView;

    class MyHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch(msg.what){
                case UPDATE_UI:
                    Info info = (Info) msg.obj;
                    time.setText(info.time);
                    title.setText(info.text);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    class Info{
        String time;
        String text;

        public Info(String time, String text) {
            this.time = time;
            this.text = text;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rainfall);
        Toolbar toolbar = findViewById(R.id.rain_toolBar);
        toolbar.setTitle("降雨预报");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        time = findViewById(R.id.rain_fixTime);
        title = findViewById(R.id.rain_title);
        recyclerView = findViewById(R.id.rain_context);
        infoList = new ArrayList<>();
        Intent intent = getIntent();
        adapter = new AirAdapter(infoList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        mapView = findViewById(R.id.rain_map);
        mapView.onCreate(savedInstanceState);
        if(amap == null){
            amap = mapView.getMap();
        }
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.amap));
        myLocationStyle.strokeColor(Color.parseColor("#7E2196F3"));
        myLocationStyle.radiusFillColor(Color.parseColor("#6F0AADF6"));
        amap.setMyLocationStyle(myLocationStyle);
        amap.getUiSettings().setMyLocationButtonEnabled(true);
        amap.setMyLocationEnabled(true);
        amap.setOnMapClickListener(latLng -> {
            if (amap != null) {
                amap.clear();
            }
            QWeather.getMinuteLy(RainfallActivity.this, latLng.longitude+","+latLng.latitude, Lang.ZH_HANS, new QWeather.OnResultMinutelyListener() {
                @Override
                public void onError(Throwable throwable) {
                }
                @Override
                public void onSuccess(MinutelyBean minutelyBean) {
                    if(minutelyBean.getCode().equals("200")) {
                        String summary = minutelyBean.getSummary();
                        String time = minutelyBean.getBasic().getUpdateTime();
                        String fixTime = time.subSequence(time.indexOf('T')+1, time.indexOf('+')).toString();
                        MarkerOptions markerOption = new MarkerOptions()
                                .position(latLng)
                                .title("数据更新时间:"+fixTime)
                                .snippet(summary)
                                .draggable(true);
                        Marker marker = amap.addMarker(markerOption);
                        marker.showInfoWindow();
                    }
                }
            });
        });
        amap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(amap != null){
                    final Handler handler = new Handler();
                    final long start = SystemClock.uptimeMillis();
                    Projection proj = amap.getProjection();
                    final LatLng markerLatlng = marker.getPosition();
                    Point markerPoint = proj.toScreenLocation(markerLatlng);
                    markerPoint.offset(0, -100);
                    final LatLng startLatLng = proj.fromScreenLocation(markerPoint);
                    final long duration = 1500;
                    final Interpolator interpolator = new BounceInterpolator();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            long elapsed = SystemClock.uptimeMillis() - start;
                            float t = interpolator.getInterpolation((float) elapsed
                                    / duration);
                            double lng = t * markerLatlng.longitude + (1 - t)
                                    * startLatLng.longitude;
                            double lat = t * markerLatlng.latitude + (1 - t)
                                    * startLatLng.latitude;
                            marker.setPosition(new LatLng(lat, lng));
                            if (t < 1.0) {
                                handler.postDelayed(this, 16);
                            }
                        }
                    });
                }
                return true;
            }
        });
        CityBean cityBean = (CityBean) intent.getSerializableExtra("city");
        InternetResource.sendHttpRequest(ChooseAreaActivity.CITY_SERVICE + cityBean.getCounty() + "&adm2=" + cityBean.getCity(), new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    onFailure(call, new IOException());
                    return;
                }
                try {
                    JSONObject rootObj = new JSONObject(response.body().string());
                    if(rootObj.getString("code").equals("200")){
                        JSONArray rootArr = rootObj.getJSONArray("location");
                        JSONObject object = rootArr.getJSONObject(0);
                        QWeather.getMinuteLy(RainfallActivity.this, object.getString("lon")+","+object.getString("lat"), Lang.ZH_HANS, new QWeather.OnResultMinutelyListener() {
                            @Override
                            public void onError(Throwable throwable) {
                            }
                            @Override
                            public void onSuccess(MinutelyBean minutelyBean) {
                                if(minutelyBean.getCode().equals("200")) {
                                    String summary = minutelyBean.getSummary();
                                    String time = minutelyBean.getBasic().getUpdateTime();
                                    List<MinutelyBean.Minutely> minutelyList = minutelyBean.getMinutelyList();
                                    infoList.clear();
                                    for (MinutelyBean.Minutely minutely : minutelyList) {
                                        String fixTime = minutely.getFxTime().substring(minutely.getFxTime().indexOf('T')+1, minutely.getFxTime().indexOf('+')).toString();
                                        infoList.add(new AirInfo(fixTime, minutely.getPrecip()));
                                    }
                                    String fixTime = time.subSequence(time.indexOf('T')+1, time.indexOf('+')).toString();
                                    handler.sendMessage(handler.obtainMessage(UPDATE_UI, new Info(cityBean.getCounty()+"  "+summary, "数据更新时间："+fixTime)));
                                }
                            }
                        });
                    }
                } catch (JSONException e) {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}