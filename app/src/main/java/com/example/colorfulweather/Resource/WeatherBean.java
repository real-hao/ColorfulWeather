package com.example.colorfulweather.Resource;

public class WeatherBean {
    private int TYPE;
    private String tempMax;
    private String tempMin;
    private String text;

    public WeatherBean(int TYPE, String text, String tempMax, String tempMin) {
        this.TYPE = TYPE;
        this.tempMax = tempMax;
        this.tempMin = tempMin;
        this.text = text;
    }

    public void setTYPE(int TYPE) {
        this.TYPE = TYPE;
    }

    public void setTempMax(String tempMax) {
        this.tempMax = tempMax;
    }

    public void setTempMin(String tempMin) {
        this.tempMin = tempMin;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getTYPE() {
        return TYPE;
    }

    public String getTempMax() {
        return tempMax;
    }

    public String getTempMin() {
        return tempMin;
    }

    public String getText() {
        return text;
    }
}
