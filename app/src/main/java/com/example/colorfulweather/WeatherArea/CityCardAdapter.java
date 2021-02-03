package com.example.colorfulweather.WeatherArea;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.colorfulweather.R;
import com.example.colorfulweather.Resource.CityBean;
import com.example.colorfulweather.Resource.DrawableResource;
import com.example.colorfulweather.Resource.WeatherBean;

import java.util.List;

public class CityCardAdapter extends RecyclerView.Adapter<CityCardAdapter.ViewHolder> {
    private List<CityBean> cityBeans;
    private List<WeatherBean> weatherBeans;
    private ChooseAreaActivity.MyListener listener;

    public CityCardAdapter(List<CityBean> cityBeans, List<WeatherBean> weatherBeans, ChooseAreaActivity.MyListener listener) {
        this.cityBeans = cityBeans;
        this.weatherBeans = weatherBeans;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weather_city_card_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(v -> {
            listener.onClick(viewHolder.getAdapterPosition());
        });
        view.setOnLongClickListener(v -> {
            listener.onLongClick(viewHolder.getAdapterPosition(), v);
            return true;
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CityBean cityBean = cityBeans.get(position);
        WeatherBean weatherBean = weatherBeans.get(position);
        holder.cityName.setText(cityBean.getCity().equals(cityBean.getCounty())? cityBean.getCounty() : cityBean.getCounty()+" · "+cityBean.getCity());
        holder.cityInfo.setText(weatherBean.getTempMax()+"°/"+weatherBean.getTempMin()+"°");
        holder.cityTemp.setText(weatherBean.getTextDay().equals(weatherBean.getTextNight())? weatherBean.getTextDay() : weatherBean.getTextDay()+"转"+weatherBean.getTextNight());
        holder.layout.setBackgroundResource(DrawableResource.getTextResource(weatherBean.getTextDay()).getImage());
        holder.imageView.setBackgroundResource(DrawableResource.getTextResource(weatherBean.getTextDay()).getIcon());
    }

    @Override
    public int getItemCount() {
        return cityBeans.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView cityName;
        TextView cityInfo;
        TextView cityTemp;
        RelativeLayout layout;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cityName = itemView.findViewById(R.id.cityName);
            cityInfo = itemView.findViewById(R.id.cityInfo);
            cityTemp = itemView.findViewById(R.id.cityTemp);
            layout = itemView.findViewById(R.id.layout);
            imageView = itemView.findViewById(R.id.image);
        }
    }
}
