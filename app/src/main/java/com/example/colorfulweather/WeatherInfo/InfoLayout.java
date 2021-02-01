package com.example.colorfulweather.WeatherInfo;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.colorfulweather.R;
import com.example.colorfulweather.Resource.DrawableResource;
import com.example.colorfulweather.Resource.InfoBean;

public class InfoLayout extends RelativeLayout{
    private static final int UPDATE_UI = 0;
    private ImageView imageView;
    private TextView tempView;
    private TextView textView;
    private Handler handler = new InfoHandler();

    class InfoHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            InfoBean infoBean = (InfoBean) msg.obj;
            switch (msg.what){
                case UPDATE_UI:
                    Glide.with(getContext()).load(DrawableResource.getTextResource(infoBean.getText()).getView()).into(imageView);
                    tempView.setText(infoBean.getTemp());
                    textView.setText(infoBean.getText());
                    break;
            }
        }
    }

    public InfoLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.weather_info_layout, this);
        imageView = findViewById(R.id.imageView);
        tempView = findViewById(R.id.temp);
        textView = findViewById(R.id.text);
    }

    public void updateData(InfoBean infoBean){
        handler.sendMessage(handler.obtainMessage(UPDATE_UI, infoBean));
    }
}
