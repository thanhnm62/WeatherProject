package com.example.weatherproject.model;


import java.io.Serializable;

public class Weather {
    private String day;
    private String thu;
    private String gio;
    private String icons;
    private String status;
    private String maxTemp;
    private String minTemp;
    private String doAm;

    public Weather() {
    }

    public Weather(String day, String thu,String gio, String icons, String status, String maxTemp, String minTemp,String doAm) {
        this.day = day;
        this.thu = thu;
        this.gio = gio;
        this.icons = icons;
        this.status = status;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.doAm = doAm;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getThu() {
        return thu;
    }

    public void setThu(String thu) {
        this.thu = thu;
    }

    public void setGio(String gio) {
        this.gio = gio;
    }

    public String getGio() {
        return gio;
    }

    public String getIcons() {
        return icons;
    }

    public void setIcons(String icons) {
        this.icons = icons;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(String maxTemp) {
        this.maxTemp = maxTemp;
    }

    public String getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(String minTemp) {
        this.minTemp = minTemp;
    }

    public String getDoAm() {
        return doAm;
    }

    public void setDoAm(String doAm) {
        this.doAm = doAm;
    }
}
