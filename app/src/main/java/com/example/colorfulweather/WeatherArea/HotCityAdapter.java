package com.example.colorfulweather.WeatherArea;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.colorfulweather.R;
import com.example.colorfulweather.Resource.CityBean;

import java.util.List;

public class HotCityAdapter extends RecyclerView.Adapter<HotCityAdapter.ViewHolder>{
    private List<CityBean> cityBeans;
    private ChooseAreaActivity.MyListener listener;

    public HotCityAdapter(List<CityBean> cityBeans, ChooseAreaActivity.MyListener listener) {
        this.cityBeans = cityBeans;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hot_city_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.textView.setOnClickListener(v -> {
            listener.onClick(viewHolder.getAdapterPosition());
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(cityBeans.get(position).getCounty());
    }

    @Override
    public int getItemCount() {
            return cityBeans.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.listItem);
        }
    }
}
