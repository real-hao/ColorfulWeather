package com.example.colorfulweather.Resource;

public class DailyWeather {
    private String time;
    private String tempMax;
    private String tempMin;
    private String textDay;
    private String textNight;
    private String windDir;
    private String windScaleDay;
    private String windScaleNight;
    private String hum;

    public DailyWeather(String time, String tempMax, String tempMin, String textDay, String textNight, String windDir, String windScaleDay, String windScaleNight, String hum) {
        this.time = time;
        this.tempMax = tempMax;
        this.tempMin = tempMin;
        this.textDay = textDay;
        this.textNight = textNight;
        this.windDir = windDir;
        this.windScaleDay = windScaleDay;
        this.windScaleNight = windScaleNight;
        this.hum = hum;
    }

    public String getTime() {
        return time;
    }

    public String getTempMax() {
        return tempMax;
    }

    public String getTempMin() {
        return tempMin;
    }

    public String getTextDay() {
        return textDay;
    }

    public String getTextNight() {
        return textNight;
    }

    public String getWindDir() {
        return windDir;
    }

    public String getWindScaleDay() {
        return windScaleDay;
    }

    public String getWindScaleNight() {
        return windScaleNight;
    }

    public String getHum() {
        return hum;
    }
}
