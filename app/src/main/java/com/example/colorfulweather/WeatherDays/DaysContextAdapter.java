package com.example.colorfulweather.WeatherDays;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.colorfulweather.R;
import com.example.colorfulweather.Resource.DailyWeather;
import com.example.colorfulweather.Resource.DrawableResource;

import java.util.List;

public class DaysContextAdapter extends RecyclerView.Adapter<DaysContextAdapter.ViewHolder> {
    private List<DailyWeather> dailyWeatherList;

    public DaysContextAdapter(List<DailyWeather> dailyWeatherList) {
        this.dailyWeatherList = dailyWeatherList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_days_context_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DailyWeather dailyWeather = dailyWeatherList.get(position);
        holder.time.setText(dailyWeather.getTime().substring(dailyWeather.getTime().indexOf("-")+1));
        holder.tempMax.setText(dailyWeather.getTempMax()+"°");
        holder.tempMin.setText(dailyWeather.getTempMin()+"°");
        holder.textDay.setText(dailyWeather.getTextDay());
        holder.textNight.setText(dailyWeather.getTextNight());
        holder.windScaleDay.setText(dailyWeather.getWindScaleDay()+"级");
        holder.windScaleNight.setText(dailyWeather.getWindScaleNight()+"级");
        holder.windDir.setText(dailyWeather.getWindDir());
        holder.hum.setText(dailyWeather.getHum()+"%");
        holder.iconDay.setImageResource(DrawableResource.getTextResource(dailyWeather.getTextDay()).getIcon());
        holder.iconNight.setImageResource(DrawableResource.getTextResource(dailyWeather.getTextNight()).getIcon());
    }

    @Override
    public int getItemCount() {
        return dailyWeatherList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView iconDay;
        private ImageView iconNight;
        private TextView time;
        private TextView tempMax;
        private TextView tempMin;
        private TextView textDay;
        private TextView textNight;
        private TextView windScaleDay;
        private TextView windScaleNight;
        private TextView windDir;
        private TextView hum;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.day_time);
            tempMax = itemView.findViewById(R.id.day_tempMax);
            tempMin = itemView.findViewById(R.id.day_tempMin);
            textDay = itemView.findViewById(R.id.day_textDay);
            textNight = itemView.findViewById(R.id.day_textNight);
            windScaleDay = itemView.findViewById(R.id.day_windScaleDay);
            windScaleNight = itemView.findViewById(R.id.day_windScaleNight);
            windDir = itemView.findViewById(R.id.day_windDir);
            hum = itemView.findViewById(R.id.day_hum);
            iconDay = itemView.findViewById(R.id.day_iconDay);
            iconNight = itemView.findViewById(R.id.day_iconNight);
        }
    }
}
