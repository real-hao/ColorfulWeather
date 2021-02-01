package com.example.colorfulweather.Resource;

public class DayMsg {
    private String sunrise;
    private String sunset;
    private String windDir;
    private String hum;
    private String feel;
    private String pressure;

    public DayMsg(String sunrise, String sunset, String windDir, String hum, String feel, String pressure) {
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.windDir = windDir;
        this.hum = hum;
        this.feel = feel;
        this.pressure = pressure;
    }

    public String getSunrise() {
        return sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public String getWindDir() {
        return windDir;
    }

    public String getHum() {
        return hum;
    }

    public String getFeel() {
        return feel;
    }

    public String getPressure() {
        return pressure;
    }
}
