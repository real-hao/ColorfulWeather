package com.example.colorfulweather.Resource;

public class WeatherBean {
    private int TYPE;
    private String tempMax;
    private String tempMin;
    private String textDay;
    private String textNight;

    public WeatherBean(int TYPE, String tempMax, String tempMin, String textDay, String textNight) {
        this.TYPE = TYPE;
        this.tempMax = tempMax;
        this.tempMin = tempMin;
        this.textDay = textDay;
        this.textNight = textNight;
    }

    public int getTYPE() {
        return TYPE;
    }

    public void setTYPE(int TYPE) {
        this.TYPE = TYPE;
    }

    public String getTempMax() {
        return tempMax;
    }

    public void setTempMax(String tempMax) {
        this.tempMax = tempMax;
    }

    public String getTempMin() {
        return tempMin;
    }

    public void setTempMin(String tempMin) {
        this.tempMin = tempMin;
    }

    public String getTextDay() {
        return textDay;
    }

    public void setTextDay(String textDay) {
        this.textDay = textDay;
    }

    public String getTextNight() {
        return textNight;
    }

    public void setTextNight(String textNight) {
        this.textNight = textNight;
    }
}
