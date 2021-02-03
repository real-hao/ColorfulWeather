package com.example.colorfulweather.WeatherInfo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.colorfulweather.R;

public class ADFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ad, container, false);
        ImageView image = view.findViewById(R.id.ad_image);
        Glide.with(getContext()).load(R.drawable.ad).into(image);
        return view;
    }
}
