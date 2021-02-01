package com.example.colorfulweather.Resource;

import com.example.colorfulweather.R;

import java.util.HashMap;
import java.util.Map;

public class DrawableResource {
    private static final Map<String, S> textMap = new HashMap<>();
    private static final Map<String, P> msgMap = new HashMap<>();

    public static class S {
        int icon;
        int image;
        int view;

        public S(int icon, int view, int image) {
            this.icon = icon;
            this.view = view;
            this.image = image;
        }

        public int getIcon() {
            return icon;
        }

        public int getImage() {
            return image;
        }

        public int getView() {
            return view;
        }
    }

    public static class P {
        int src;

        public P(int src) {
            this.src = src;
        }

        public int getSrc() {
            return src;
        }
    }

    static {
        msgMap.put("穿衣指数", new P(R.drawable.clothes));
        msgMap.put("防晒指数", new P(R.drawable.spi));
        msgMap.put("运动指数", new P(R.drawable.sports));
        msgMap.put("洗车指数", new P(R.drawable.car));
        msgMap.put("交通指数", new P(R.drawable.traff));
        msgMap.put("感冒指数", new P(R.drawable.flu));
        msgMap.put("晾晒指数", new P(R.drawable.dc));
        msgMap.put("舒适度指数", new P(R.drawable.comf));

        textMap.put("晴", new S(R.drawable.sunny, R.drawable.v_clear, R.drawable.h_clear));
        textMap.put("多云", new S(R.drawable.cloudy, R.drawable.v_cloudy, R.drawable.h_cloudy));
        textMap.put("少云", new S(R.drawable.few_clouds, R.drawable.v_cloudy, R.drawable.h_cloudy));
        textMap.put("晴间多云", new S(R.drawable.partly_cloudy, R.drawable.v_cloudy, R.drawable.h_cloudy));
        textMap.put("阴", new S(R.drawable.over_cast, R.drawable.v_cloudy, R.drawable.h_cloudy));
        textMap.put("阵雨", new S(R.drawable.shower_rain, R.drawable.v_rain, R.drawable.h_rain));
        textMap.put("强阵雨", new S(R.drawable.heavy_shower_rain, R.drawable.v_rain, R.drawable.h_rain));
        textMap.put("强雷阵雨", new S(R.drawable.heavy_thunderstorm, R.drawable.v_rain, R.drawable.h_rain));
        textMap.put("强雷阵雨伴有冰雹", new S(R.drawable.thundershower_with_hail, R.drawable.v_rain, R.drawable.h_rain));
        textMap.put("小雨", new S(R.drawable.light_rain, R.drawable.v_rain, R.drawable.h_rain));
        textMap.put("中雨", new S(R.drawable.moderate_rain, R.drawable.v_rain, R.drawable.h_rain));
        textMap.put("大雨", new S(R.drawable.heavy_rain, R.drawable.v_rain, R.drawable.h_rain));
        textMap.put("极端降雨", new S(R.drawable.extreme_rain, R.drawable.v_rain, R.drawable.h_rain));
        textMap.put("毛毛雨", new S(R.drawable.drizzle_rain, R.drawable.v_rain, R.drawable.h_rain));
        textMap.put("暴雨", new S(R.drawable.storm, R.drawable.v_rain, R.drawable.h_rain));
        textMap.put("大暴雨", new S(R.drawable.heavy_storm, R.drawable.v_rain, R.drawable.h_rain));
        textMap.put("特大暴雨", new S(R.drawable.severe_storm, R.drawable.v_rain, R.drawable.h_rain));
        textMap.put("冻雨", new S(R.drawable.freezing_rain, R.drawable.v_rain, R.drawable.h_rain));
        textMap.put("小到中雨", new S(R.drawable.light_to_moderate_rain, R.drawable.v_rain, R.drawable.h_rain));
        textMap.put("中到大雨", new S(R.drawable.moderate_to_heavy_rain, R.drawable.v_rain, R.drawable.h_rain));
        textMap.put("大到暴雨", new S(R.drawable.heavy_rain_to_storm, R.drawable.v_rain, R.drawable.h_rain));
        textMap.put("暴雨到大暴雨", new S(R.drawable.storm_to_heavy_storm, R.drawable.v_rain, R.drawable.h_rain));
        textMap.put("大暴雨到特大暴雨", new S(R.drawable.heavy_to_severe_storm, R.drawable.v_rain, R.drawable.h_rain));
        textMap.put("雨", new S(R.drawable.rain, R.drawable.v_rain, R.drawable.h_rain));
        textMap.put("小雪", new S(R.drawable.light_snow, R.drawable.v_snow, R.drawable.h_snow));
        textMap.put("中雪", new S(R.drawable.moderate_snow, R.drawable.v_snow, R.drawable.h_snow));
        textMap.put("大雪", new S(R.drawable.heavy_snow, R.drawable.v_snow, R.drawable.h_snow));
        textMap.put("暴雪", new S(R.drawable.snowstorm, R.drawable.v_snow, R.drawable.h_snow));
        textMap.put("雨夹雪", new S(R.drawable.sleet, R.drawable.v_snow, R.drawable.h_snow));
        textMap.put("雨雪天气", new S(R.drawable.rain_and_snow, R.drawable.v_snow, R.drawable.h_snow));
        textMap.put("阵雨夹雪", new S(R.drawable.shower_snow, R.drawable.v_snow, R.drawable.h_snow));
        textMap.put("阵雪", new S(R.drawable.snow_flurry, R.drawable.v_snow, R.drawable.h_snow));
        textMap.put("小到中雪", new S(R.drawable.light_to_moderate_snow, R.drawable.v_snow, R.drawable.h_snow));
        textMap.put("中到大雪", new S(R.drawable.moderate_to_heavy_snow, R.drawable.v_snow, R.drawable.h_snow));
        textMap.put("大到暴雪", new S(R.drawable.heavy_snow_to_snowstorm, R.drawable.v_snow, R.drawable.h_snow));
        textMap.put("雪", new S(R.drawable.snow, R.drawable.v_snow, R.drawable.h_snow));
        textMap.put("薄雾", new S(R.drawable.mist, R.drawable.v_haze, R.drawable.h_haze));
        textMap.put("雾", new S(R.drawable.foggy, R.drawable.v_haze, R.drawable.h_haze));
        textMap.put("霾", new S(R.drawable.haze, R.drawable.v_haze, R.drawable.h_haze));
        textMap.put("扬沙", new S(R.drawable.sand, R.drawable.v_sand, R.drawable.h_sand));
        textMap.put("浮尘", new S(R.drawable.dust, R.drawable.v_sand, R.drawable.h_sand));
        textMap.put("沙尘暴", new S(R.drawable.dust_storm, R.drawable.v_sand, R.drawable.h_sand));
        textMap.put("强沙尘暴", new S(R.drawable.sandstorm, R.drawable.v_sand, R.drawable.h_sand));
        textMap.put("浓雾", new S(R.drawable.dense_fog, R.drawable.v_haze, R.drawable.h_haze));
        textMap.put("强浓雾", new S(R.drawable.strong_fog, R.drawable.v_haze, R.drawable.h_haze));
        textMap.put("中度霾", new S(R.drawable.moderate_haze, R.drawable.v_haze, R.drawable.h_haze));
        textMap.put("重度霾", new S(R.drawable.heavy_haze, R.drawable.v_haze, R.drawable.h_haze));
        textMap.put("严重霾", new S(R.drawable.severe_haze, R.drawable.v_haze, R.drawable.h_haze));
        textMap.put("大雾", new S(R.drawable.heavy_fog, R.drawable.v_haze, R.drawable.h_haze));
        textMap.put("特强浓雾", new S(R.drawable.extra_heavy_fog, R.drawable.v_haze, R.drawable.h_haze));
        textMap.put("热", new S(R.drawable.hot, R.drawable.v_clear, R.drawable.h_clear));
        textMap.put("冷", new S(R.drawable.cold, R.drawable.v_clear_night, R.drawable.h_clear_night));
        textMap.put("未知", new S(R.drawable.unknow, R.drawable.err404, R.drawable.err404));
    }

    public static S getTextResource(String text){
        S s = textMap.get(text);
        if(s == null){
            s = textMap.get("未知");
        }
        return s;
    }

    public static int getMsgResource(String s){
        P p = msgMap.get(s);
        if(p == null){
            p = new P(R.drawable.error);
        }
        return p.src;
    }
}
