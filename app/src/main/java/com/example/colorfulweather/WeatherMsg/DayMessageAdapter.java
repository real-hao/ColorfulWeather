package com.example.colorfulweather.WeatherMsg;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.colorfulweather.R;
import com.example.colorfulweather.Resource.DayMsg;

import org.w3c.dom.Text;

import java.util.Map;

public class DayMessageAdapter extends RecyclerView.Adapter<DayMessageAdapter.ViewHolder> {
    private Map<DayMsgType, String> dayMsgList;

    public DayMessageAdapter(Map<DayMsgType, String> dayMsgList) {
        this.dayMsgList = dayMsgList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weather_message_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        switch (position){
            case 0:
                holder.msgTop.setText(dayMsgList.get(DayMsgType.WIND_SCALE)+"级");
                holder.msgBottom.setText(dayMsgList.get(DayMsgType.WIND_DIR));
                break;
            case 1:
                holder.msgTop.setText(dayMsgList.get(DayMsgType.HUM)+"%");
                holder.msgBottom.setText("湿度");
                break;
            case 2:
                holder.msgTop.setText(dayMsgList.get(DayMsgType.FEEL_LIKE)+"°");
                holder.msgBottom.setText("体感");
                break;
            case 3:
                holder.msgTop.setText(dayMsgList.get(DayMsgType.PRESSURE));
                holder.msgBottom.setText("大气压");
                break;
        }
    }

    @Override
    public int getItemCount() {
        return dayMsgList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView msgTop;
        TextView msgBottom;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            msgTop = itemView.findViewById(R.id.msgTop);
            msgBottom = itemView.findViewById(R.id.msgBottom);
        }
    }
}
