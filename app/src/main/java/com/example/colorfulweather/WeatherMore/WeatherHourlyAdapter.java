package com.example.colorfulweather.WeatherMore;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.colorfulweather.R;
import com.example.colorfulweather.Resource.DrawableResource;
import com.example.colorfulweather.Resource.WeatherHourly;

import java.util.List;

public class WeatherHourlyAdapter extends RecyclerView.Adapter<WeatherHourlyAdapter.ViewHolder>{
    private List<WeatherHourly> hourlies;

    public WeatherHourlyAdapter(List<WeatherHourly> hourlies) {
        this.hourlies = hourlies;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hourly_weather_more_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WeatherHourly weatherHourly = hourlies.get(position);
        holder.time.setText(weatherHourly.getTime());
        holder.text.setText(weatherHourly.getText());
        holder.temp.setText(weatherHourly.getTemp()+"°");
        holder.icon.setImageResource(DrawableResource.getTextResource(weatherHourly.getText()).getIcon());
        holder.windDir.setText(weatherHourly.getWindDir());
        holder.windScale.setText(weatherHourly.getWindScale()+"级");
    }

    @Override
    public int getItemCount() {
        return hourlies.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView time;
        TextView text;
        TextView temp;
        ImageView icon;
        TextView windScale;
        TextView windDir;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.hourly_time);
            text = itemView.findViewById(R.id.hourly_text);
            temp = itemView.findViewById(R.id.hourly_temp);
            icon = itemView.findViewById(R.id.hourly_icon);
            windScale = itemView.findViewById(R.id.hourly_windScale);
            windDir = itemView.findViewById(R.id.hourly_windDir);
        }
    }
}
