package com.example.colorfulweather.Resource;

import java.io.Serializable;

public class CityBean implements Serializable {
    private String id;
    private String country;
    private String province;
    private String city;
    private String county;

    public CityBean(String id, String country, String province, String city, String county) {
        this.id = id;
        this.country = country;
        this.province = province;
        this.city = city;
        this.county = county;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

}
