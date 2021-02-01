package com.example.colorfulweather.WeatherMsg;

import android.content.res.Resources;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.colorfulweather.R;
import com.example.colorfulweather.Resource.DrawableResource;
import com.example.colorfulweather.Resource.SuggestionMsg;

import java.util.List;

public class SuggestionAdapter extends RecyclerView.Adapter<SuggestionAdapter.ViewHolder>{

    private List<SuggestionMsg> msgList;
    private Resources resources;

    public SuggestionAdapter(List<SuggestionMsg> msgList, Resources resources) {
        this.msgList = msgList;
        this.resources = resources;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weather_suggestion_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(v -> {
            View popView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pop_window_layout, null, false);
            TextView name = popView.findViewById(R.id.pop_name);
            TextView text = popView.findViewById(R.id.pop_text);
            int pos = viewHolder.getAdapterPosition();
            name.setText(msgList.get(pos).getName()+"  "+msgList.get(pos).getCategory());
            text.setText(msgList.get(pos).getText());
            PopupWindow pop = new PopupWindow(popView,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    resources.getDisplayMetrics().heightPixels/3,
                    true);
            pop.setBackgroundDrawable(resources.getDrawable(R.drawable.pop_window_background, null));
            pop.setOutsideTouchable(true);
            pop.setClippingEnabled(false);
            pop.setAnimationStyle(R.style.PopupWindow_anim_style);
            pop.showAtLocation(popView, Gravity.BOTTOM, 0, 0);
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SuggestionMsg msg = msgList.get(position);
        holder.pic.setImageResource(DrawableResource.getMsgResource(msg.getName()));
        holder.category.setText(msg.getCategory());
        holder.name.setText(msg.getName());
    }

    @Override
    public int getItemCount() {
        return msgList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView pic;
        TextView name;
        TextView category;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pic = itemView.findViewById(R.id.item_pic);
            name = itemView.findViewById(R.id.item_name);
            category = itemView.findViewById(R.id.item_category);
        }
    }
}
