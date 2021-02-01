package com.example.colorfulweather.Resource;

public class WeatherHourly {
    private String time;
    private String temp;
    private String text;
    private String windScale;
    private String windDir;

    public WeatherHourly(String time, String temp, String text, String windScale, String windDir) {
        this.time = time;
        this.temp = temp;
        this.text = text;
        this.windScale = windScale;
        this.windDir = windDir;
    }

    public String getTime() {
        return time;
    }

    public String getTemp() {
        return temp;
    }

    public String getText() {
        return text;
    }

    public String getWindScale() {
        return windScale;
    }

    public String getWindDir() {
        return windDir;
    }
}
