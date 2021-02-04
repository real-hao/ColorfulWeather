package com.example.colorfulweather.WeatherSettings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.colorfulweather.R;
import com.example.colorfulweather.Resource.CityBean;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {
    private List<SettingItem> list = new ArrayList<>();
    private SharedPreferences.Editor editor;
    private CityBean cityBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        editor = getSharedPreferences("settings", MODE_PRIVATE).edit();
        Toolbar toolbar = findViewById(R.id.settings_toolBar);
        toolbar.setTitle("设置");
        cityBean = (CityBean) getIntent().getSerializableExtra("data");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        RecyclerView recyclerView = findViewById(R.id.settings_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        initView();
        recyclerView.setAdapter(new SettingsAdapter(list));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    private void initView() {
        SharedPreferences preferences = getSharedPreferences("settings", MODE_PRIVATE);
        list.add(new SettingItem("天气提醒", "早晚天气提醒", "每天7点/19点左右为您推送今天/明天的天气", (buttonView, isChecked) -> {
            if (!buttonView.isPressed()) {
                return;
            }
            editor.putBoolean("dailyReminder", isChecked);
            editor.commit();
        }, null, preferences.getBoolean("dailyReminder", true)));

        list.add(new SettingItem(null, "天气预警提醒", "您会收到气象灾害预警推送", (buttonView, isChecked) -> {
            if (!buttonView.isPressed()) {
                return;
            }
            editor.putBoolean("warnReminder", isChecked);
            editor.commit();
        }, null, preferences.getBoolean("warnReminder", true)));
        list.add(new SettingItem(null, "常驻天气信息到通知栏", "在通知栏显示定位所在地详细的实时天气", (buttonView, isChecked) -> {
            if (!buttonView.isPressed()) {
                return;
            }
            Intent intent = new Intent(SettingsActivity.this, MyService.class);
            if(isChecked){
                intent.putExtra("data", cityBean);
                startService(intent);
            }else{
                stopService(intent);
            }
            editor.putBoolean("showNotification", isChecked);
            editor.commit();
        }, null, preferences.getBoolean("showNotification", true)));
        list.add(new SettingItem(null, "夜间免打扰", "23:00 - 7:00不推送上述天气消息", (buttonView, isChecked) -> {
            if (!buttonView.isPressed()) {
                return;
            }
            editor.putBoolean("disReminder", isChecked);
            editor.commit();
        }, null, preferences.getBoolean("disReminder", true)));
        list.add(new SettingItem("关于天气", "软件更新", "单击view code下载apk文件", null, v -> {
            Intent intent= new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse("https://github.com/real-hao/ColorfulWeather");
            intent.setData(content_url);
            startActivity(intent);
        }, null));
        list.add(new SettingItem(null, "隐私声明", null, null, v -> {
            startActivity(new Intent(SettingsActivity.this, WebViewActivity.class));
        }, null));
        list.add(new SettingItem(null, "关于天气", null, null, v -> {
            Toast.makeText(this, "version 1.0", Toast.LENGTH_SHORT).show();
        }, null));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}