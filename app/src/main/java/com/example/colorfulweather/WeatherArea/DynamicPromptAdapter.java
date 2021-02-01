package com.example.colorfulweather.WeatherArea;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.colorfulweather.R;
import com.example.colorfulweather.Resource.CityBean;

import java.util.List;

public class DynamicPromptAdapter extends ArrayAdapter<CityBean> {

    private int resourceId;

    public DynamicPromptAdapter(@NonNull Context context, int resource, @NonNull List<CityBean> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        CityBean cityBean = getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textView = view.findViewById(R.id.simpleText);
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.textView.setText(cityBean.getCounty()+" · "+cityBean.getCity()+" · "+cityBean.getCountry());
        return view;
    }

    class ViewHolder{
        TextView textView;
    }
}
