package com.example.colorfulweather.WeatherArea;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.colorfulweather.Resource.CityBean;

import java.util.List;

public class CityCardAdapter extends RecyclerView.Adapter<CityCardAdapter.ViewHolder> {
    private List<CityBean> cityBeans;

    public CityCardAdapter(List<CityBean> cityBeans) {
        this.cityBeans = cityBeans;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // ...
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
