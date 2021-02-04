package com.example.colorfulweather.WeatherSettings;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import com.example.colorfulweather.Resource.CityBean;
import com.example.colorfulweather.Resource.DrawableResource;
import com.example.colorfulweather.WeatherInfo.MainActivity;
import com.qweather.sdk.bean.weather.WeatherNowBean;
import com.qweather.sdk.view.QWeather;

public class MyService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null){
            CityBean cityBean = (CityBean) intent.getSerializableExtra("data");
            new Thread(() -> {
                QWeather.getWeatherNow(MyService.this, cityBean.getId(), new QWeather.OnResultWeatherNowListener(){
                    @Override
                    public void onError(Throwable throwable) {
                        stopSelf();
                    }
                    @Override
                    public void onSuccess(WeatherNowBean weatherNowBean) {
                        if (weatherNowBean.getCode().equals("200")){
                            WeatherNowBean.NowBaseBean nowBaseBean = weatherNowBean.getNow();
                            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                NotificationChannel channel = new NotificationChannel("normal", "常驻通知栏", NotificationManager.IMPORTANCE_DEFAULT);
                                channel.setDescription("可在主页>设置>天气设置中查看");
                                manager.createNotificationChannel(channel);
                            }
                            Intent intent = new Intent(MyService.this, MainActivity.class);
                            PendingIntent pi = PendingIntent.getActivity(MyService.this, 0, intent, 0);
                            Notification notification = new NotificationCompat.Builder(MyService.this, "normal")
                                    .setContentTitle(cityBean.getCounty()+"的实时天气")
                                    .setContentText("Because of you, life can be colourful")
                                    .setStyle(new NotificationCompat.BigTextStyle().bigText(
                                            "天气："+nowBaseBean.getText()+ "  温度："+nowBaseBean.getTemp()+ "°  体感："+nowBaseBean.getFeelsLike()+"°\n"+
                                                    "湿度："+nowBaseBean.getHumidity()+ "%  云量："+nowBaseBean.getDew()+ "  降雨量："+nowBaseBean.getPrecip()+"\n"+
                                                    "大气压："+nowBaseBean.getPressure()+ "hpa  风向："+nowBaseBean.getWindDir()+ "  风力："+nowBaseBean.getWindScale()+"级\n"
                                    ))
                                    .setSmallIcon(DrawableResource.getTextResource(nowBaseBean.getText()).getIcon())
                                    .setContentIntent(pi)
                                    .setAutoCancel(true)
                                    .build();
                            startForeground(1, notification);
                        }else{
                            stopSelf();
                        }
                    }
                });
            }).start();
        }else{
            stopSelf();
        }
        return super.onStartCommand(intent, flags, startId);
    }
}