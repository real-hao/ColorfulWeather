package com.example.colorfulweather.WeatherAir;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.MenuItem;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.Projection;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.autonavi.amap.mapcore.interfaces.IMarker;
import com.example.colorfulweather.R;
import com.example.colorfulweather.Resource.AirInfo;
import com.example.colorfulweather.Resource.CityBean;
import com.qweather.sdk.bean.air.AirDailyBean;
import com.qweather.sdk.bean.air.AirNowBean;
import com.qweather.sdk.bean.base.Lang;
import com.qweather.sdk.view.QWeather;

import java.util.ArrayList;
import java.util.List;

public class AirContextActivity extends AppCompatActivity {
    private static final int UPDATE_UI = 0;
    private static final int UPDATE_15D = 1;
    private static final int TOAST_MESSAGE = 2;
    private List<AirInfo> airInfoList;
    private TextView time;
    private TextView title;
    private RecyclerView airContext;
    private AirAdapter airAdapter;
    private RecyclerView air15d;
    private Air15DAdapter air15dAdapter;
    private List<AirInfo> air15dList;
    private MapView mapView;
    private AMap amap;
    private MyHandler handler = new MyHandler();

    class Info{
        String time;
        String county;
        String aqi;
        String category;

        public Info(String time, String county, String aqi, String level, String category) {
            this.time = time;
            this.county = county;
            this.aqi = aqi;
            this.category = category;
        }
    }

    class MyHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case UPDATE_UI:
                    Info info = (Info) msg.obj;
                    time.setText(info.county+"  更新于"+info.time);
                    title.setText(info.aqi+"  "+info.category);
                    airAdapter.notifyDataSetChanged();
                break;
                case UPDATE_15D:
                    air15dAdapter.notifyDataSetChanged();
                    break;
                case TOAST_MESSAGE:
                    Toast.makeText(AirContextActivity.this, (String)msg.obj, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_air_context);
        Intent intent = getIntent();
        if(intent == null){
            return;
        }
        Toolbar toolbar = findViewById(R.id.air_toolBar);
        toolbar.setTitle("空气质量");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        time = findViewById(R.id.air_fixTime);
        title = findViewById(R.id.air_title);
        airContext = findViewById(R.id.air_context);
        airAdapter = new AirAdapter(airInfoList);
        mapView = findViewById(R.id.air_map);
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
            if(amap != null){
                amap.clear();
            }
            QWeather.getAirNow(AirContextActivity.this, ""+latLng.longitude+","+latLng.latitude, Lang.ZH_HANS, new QWeather.OnResultAirNowListener(){
                @Override
                public void onError(Throwable throwable) {
                    handler.sendMessage(handler.obtainMessage(TOAST_MESSAGE, "无法连接服务器"));
                }
                @Override
                public void onSuccess(AirNowBean airNowBean) {
                    if(airNowBean.getCode().equals("200")){
                        AirNowBean.NowBean nowBean = airNowBean.getNow();
                        MarkerOptions markerOption = new MarkerOptions()
                                .position(latLng)
                                .title(nowBean.getAqi() +" "+ nowBean.getCategory())
                                .snippet("空气质量指数:"+nowBean.getLevel())
                                .draggable(true);
                        Marker marker = amap.addMarker(markerOption);
                        marker.showInfoWindow();
                    }else{
                        handler.sendMessage(handler.obtainMessage(TOAST_MESSAGE, "该区域暂无数据呢"));
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
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        airContext.setLayoutManager(manager);
        airInfoList = new ArrayList<>();
        airAdapter = new AirAdapter(airInfoList);
        airContext.setAdapter(airAdapter);
        air15d = findViewById(R.id.air15d);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        air15d.setLayoutManager(linearLayoutManager);
        air15dList = new ArrayList<>();
        air15dAdapter = new Air15DAdapter(air15dList);
        air15d.setAdapter(air15dAdapter);
        CityBean cityBean = (CityBean) intent.getSerializableExtra("city");
        QWeather.getAirNow(this, cityBean.getId(), Lang.ZH_HANS, new QWeather.OnResultAirNowListener(){
            @Override
            public void onError(Throwable throwable) {
            }
            @Override
            public void onSuccess(AirNowBean airNowBean) {
                if(airNowBean.getCode().equals("200")){
                    AirNowBean.NowBean nowBean = airNowBean.getNow();
                    String time = nowBean.getPubTime().substring(0, nowBean.getPubTime().indexOf('+'));
                    Info info = new Info(time, cityBean.getCounty(), nowBean.getAqi(), nowBean.getLevel(), nowBean.getCategory());
                    airInfoList.clear();
                    airInfoList.add(new AirInfo("指数等级", nowBean.getLevel()));
                    airInfoList.add(new AirInfo("pm10", nowBean.getPm10()));
                    airInfoList.add(new AirInfo("pm2.5", nowBean.getPm2p5()));
                    airInfoList.add(new AirInfo("No2", nowBean.getNo2()));
                    airInfoList.add(new AirInfo("So2", nowBean.getSo2()));
                    airInfoList.add(new AirInfo("Co", nowBean.getCo()));
                    airInfoList.add(new AirInfo("O3", nowBean.getO3()));
                    handler.sendMessage(handler.obtainMessage(UPDATE_UI, info));
                }
            }
        });
        QWeather.getAir5D(this, cityBean.getId(), Lang.ZH_HANS, new QWeather.OnResultAirDailyListener(){
            @Override
            public void onError(Throwable throwable) {
            }
            @Override
            public void onSuccess(AirDailyBean airDailyBean) {
                if(airDailyBean.getCode().equals("200")){
                    List<AirDailyBean.DailyBean> dailyBeans = airDailyBean.getAirDaily();
                    air15dList.clear();
                    for (AirDailyBean.DailyBean dailyBean : dailyBeans) {
                        String time = dailyBean.getFxDate().substring(dailyBean.getFxDate().indexOf('-')+1);
                        air15dList.add(new AirInfo(
                                time,
                                dailyBean.getAqi(),
                        dailyBean.getCategory()
                        ));
                    }
                    handler.sendEmptyMessage(UPDATE_15D);
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