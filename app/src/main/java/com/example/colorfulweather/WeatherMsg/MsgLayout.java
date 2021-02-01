package com.example.colorfulweather.WeatherMsg;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.colorfulweather.R;
import com.example.colorfulweather.Resource.SuggestionMsg;
import com.qweather.sdk.bean.IndicesBean;
import com.qweather.sdk.bean.weather.WeatherNowBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MsgLayout extends LinearLayout {
    private static final int UPDATE_SUGGESTION = 0;
    private static final int UPDATE_DAY = 1;
    private static final int UPDATE_SUN_INFO = 2;
    private TextView sunrise, sunset;
    private Map<DayMsgType, String> dayMsgList;
    private RecyclerView day;
    private List<SuggestionMsg> suggestionMsgList;
    private RecyclerView suggestion;
    private DayMessageAdapter dayAdapter;
    private SuggestionAdapter suggestionAdapter;
    private MsgHandler handler = new MsgHandler();

    class MsgHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case UPDATE_SUGGESTION:
                    suggestionAdapter.notifyDataSetChanged();
                    break;
                case UPDATE_DAY:
                    dayAdapter.notifyDataSetChanged();
                    break;
                case UPDATE_SUN_INFO:
                    String[] s = (String[]) msg.obj;
                    sunrise.setText(s[0]);
                    sunset.setText(s[1]);
                    break;
                default:
                    break;
            }
        }
    }

    public MsgLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.weather_msg_layout, this);
        sunrise = findViewById(R.id.msg_sunrise);
        sunset = findViewById(R.id.msg_sunset);
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        dayMsgList = new HashMap<>();
        day = findViewById(R.id.dayMessage);
        day.setLayoutManager(gridLayoutManager);
        dayAdapter = new DayMessageAdapter(dayMsgList);
        day.setAdapter(dayAdapter);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        suggestionMsgList = new ArrayList<>();
        suggestion = findViewById(R.id.suggestion);
        suggestion.setLayoutManager(manager);
        suggestionAdapter = new SuggestionAdapter(suggestionMsgList, getResources());
        suggestion.setAdapter(suggestionAdapter);
    }

    public void updateDayMsg(WeatherNowBean.NowBaseBean nowBaseBean){
        dayMsgList.put(DayMsgType.WIND_DIR, nowBaseBean.getWindDir());
        dayMsgList.put(DayMsgType.WIND_SCALE, nowBaseBean.getWindScale());
        dayMsgList.put(DayMsgType.HUM, nowBaseBean.getHumidity());
        dayMsgList.put(DayMsgType.PRESSURE, nowBaseBean.getPressure());
        dayMsgList.put(DayMsgType.FEEL_LIKE, nowBaseBean.getFeelsLike());
        handler.sendEmptyMessage(UPDATE_DAY);
    }

    public void updateSunriseAndSunset(String[] sunriseAndSunset){
        handler.sendMessage(handler.obtainMessage(UPDATE_SUN_INFO, sunriseAndSunset));
    }

    public void updateSuggestion(List<IndicesBean.DailyBean> dailyBeans){
        suggestionMsgList.clear();
        for (IndicesBean.DailyBean dailyBean : dailyBeans) {
            suggestionMsgList.add(new SuggestionMsg(dailyBean.getText(), dailyBean.getCategory(), dailyBean.getName()));
        }
        handler.sendEmptyMessage(UPDATE_SUGGESTION);
    }
}
