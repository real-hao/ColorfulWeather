package com.example.colorfulweather.WeatherMore;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.colorfulweather.R;
import com.example.colorfulweather.Resource.DrawableResource;
import com.example.colorfulweather.Resource.WeatherBean;

public class WeatherMoreItemLayout extends RelativeLayout {
    public static final int TYPE_TODAY = 0;
    public static final int TYPE_TOM = 1;
    public static final int TYPE_ATOM = 2;

    private static final int UPDATE_UI = 3;

    private TextView today_day;
    private TextView today_text;
    private TextView today_temp;
    private ImageView today_icon;
    private MoreItemHandler handler = new MoreItemHandler();

    class MoreItemHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case UPDATE_UI:
                    WeatherBean weatherBean = (WeatherBean) msg.obj;
                    switch(weatherBean.getTYPE()){
                        case TYPE_TODAY:
                            today_day.setText("今天 · ");
                            break;
                        case TYPE_TOM:
                            today_day.setText("明天 · ");
                            break;
                        case TYPE_ATOM:
                            today_day.setText("后天 · ");
                            break;
                    }
                today_text.setText(weatherBean.getTextDay().equals(weatherBean.getTextNight()) ? weatherBean.getTextDay() : weatherBean.getTextDay()+"转"+weatherBean.getTextNight());
                today_temp.setText(weatherBean.getTempMax()+"°/"+weatherBean.getTempMin()+"°");
                today_icon.setImageResource(DrawableResource.getTextResource(weatherBean.getTextDay()).getIcon());
                break;
            }
        }
    }

    public WeatherMoreItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.item_weather_more_layout, this);
        today_day = findViewById(R.id.today_day);
        today_text = findViewById(R.id.today_text);
        today_temp = findViewById(R.id.today_temp);
        today_icon = findViewById(R.id.today_icon);
    }

    public void update(WeatherBean weatherBean) {
        handler.sendMessage(handler.obtainMessage(UPDATE_UI, weatherBean));
    }
}
