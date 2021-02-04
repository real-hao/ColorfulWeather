package com.example.colorfulweather.WeatherSettings;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.colorfulweather.R;

import java.util.List;

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.ViewHolder> {
    private List<SettingItem> list;
    private View view;

    public SettingsAdapter(List<SettingItem> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_settings_view_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SettingItem item = list.get(holder.getAdapterPosition());
        if(item.getHead() != null){
            holder.head.setText(item.getHead());
        }else{
            holder.head.setVisibility(View.GONE);
        }
        holder.title.setText(item.getTitle());
        if(item.getSubtitle() != null){
            holder.subtitle.setText(item.getSubtitle());
        }else{
            holder.subtitle.setVisibility(View.GONE);
        }
        if(item.getChangeListener() == null){
            holder.switcher.setVisibility(View.GONE);
            view.setOnClickListener(item.getOnClickListener());
        }else{
            holder.switcher.setChecked(item.getIsChecked());
            holder.switcher.setOnCheckedChangeListener(item.getChangeListener());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView head;
        TextView title;
        TextView subtitle;
        Switch switcher;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            head = itemView.findViewById(R.id.setting_head);
            title = itemView.findViewById(R.id.setting_title);
            subtitle = itemView.findViewById(R.id.setting_subtitle);
            switcher = itemView.findViewById(R.id.setting_switcher);
        }
    }
}
