package com.example.colorfulweather.WeatherAir;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.colorfulweather.R;
import com.example.colorfulweather.Resource.AirInfo;

import java.util.List;

public class Air15DAdapter extends RecyclerView.Adapter<Air15DAdapter.ViewHolder> {
    private List<AirInfo> airInfoList;

    public Air15DAdapter(List<AirInfo> airInfoList) {
        this.airInfoList = airInfoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_air_15d_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AirInfo info = airInfoList.get(position);
        holder.day.setText(info.getType());
        holder.aqi.setText(info.getData());
        holder.level.setText(info.getDataMore());
    }

    @Override
    public int getItemCount() {
        return airInfoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView day;
        TextView aqi;
        TextView level;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.d15_day);
            aqi = itemView.findViewById(R.id.d15_aqi);
            level = itemView.findViewById(R.id.d15_level);
        }
    }

}
