package com.example.colorfulweather.Resource;

public class InfoBean {
    private String temp;
    private String text;

    public InfoBean(String temp, String text) {
        this.temp = temp;
        this.text = text;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
