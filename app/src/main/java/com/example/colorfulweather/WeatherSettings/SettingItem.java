package com.example.colorfulweather.WeatherSettings;

import android.view.View;
import android.widget.CompoundButton;

public class SettingItem {
    private String head;
    private String title;
    private String subtitle;
    private Boolean isChecked;
    private CompoundButton.OnCheckedChangeListener changeListener;
    private View.OnClickListener onClickListener;

    public SettingItem(String head, String title, String subtitle, CompoundButton.OnCheckedChangeListener changeListener, View.OnClickListener onClickListener, Boolean isChecked) {
        this.head = head;
        this.title = title;
        this.subtitle = subtitle;
        this.changeListener = changeListener;
        this.onClickListener = onClickListener;
        this.isChecked = isChecked;
    }

    public String getHead() {
        return head;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public CompoundButton.OnCheckedChangeListener getChangeListener() {
        return changeListener;
    }

    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public Boolean getIsChecked() {
        return isChecked;
    }
}
