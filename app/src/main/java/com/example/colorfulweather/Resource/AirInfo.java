package com.example.colorfulweather.Resource;

public class AirInfo {
    private String type;
    private String data;
    private String dataMore;

    public AirInfo(String type, String data, String dataMore) {
        this.type = type;
        this.data = data;
        this.dataMore = dataMore;
    }

    public AirInfo(String type, String data) {
        this.type = type;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDataMore() {
        return dataMore;
    }

    public void setDataMore(String dataMore) {
        this.dataMore = dataMore;
    }
}
