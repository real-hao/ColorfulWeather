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

public class AirAdapter extends RecyclerView.Adapter<AirAdapter.ViewHolder> {
    private List<AirInfo> airInfoList;

    public AirAdapter(List<AirInfo> airInfoList) {
        this.airInfoList = airInfoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_air_content_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AirInfo airInfo = airInfoList.get(position);
        holder.textView1.setText(airInfo.getData());
        holder.textView2.setText(airInfo.getType());
    }

    @Override
    public int getItemCount() {
        return airInfoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView1;
        TextView textView2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.air_text1);
            textView2 = itemView.findViewById(R.id.air_text2);
        }
    }
}
