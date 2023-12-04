package org.example.model;

import java.time.Instant;

public class Weather {
    private final double temp;
    private final double precipitation;
    private final int humidity;
    private final int clouds;
    private final double windSpeed;
    private final Instant predictionTime;
    private final Location location;
    private Instant ts;
    private String ss;

    public Weather(double temp, double precipitation, int humidity, int clouds, double windSpeed, Instant predictionTime, Location location, Instant ts, String ss) {
        this.temp = temp;
        this.precipitation = precipitation;
        this.humidity = humidity;
        this.clouds = clouds;
        this.windSpeed = windSpeed;
        this.predictionTime = predictionTime;
        this.location = location;
        this.ts = ts;
        this.ss = ss;
    }

    public double getTemp() {
        return temp;
    }

    public double getPrecipitation() {
        return precipitation;
    }

    public int getHumidity() {
        return humidity;
    }

    public int getClouds() {
        return clouds;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public Instant getPredictionTime() {
        return predictionTime;
    }

    public Location getLocation() {
        return location;
    }

    public Instant getTs() {
        return ts;
    }

    public void setTs(Instant ts) {
        this.ts = ts;
    }

    public String getSs() {
        return ss;
    }

    public void setSs(String ss) {
        this.ss = ss;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "temp=" + temp +
                ", precipitation=" + precipitation +
                ", humidity=" + humidity +
                ", clouds=" + clouds +
                ", windSpeed=" + windSpeed +
                ", predictionTime=" + predictionTime +
                ", location=" + location +
                ", ts=" + ts +
                ", ss='" + ss + '\'' +
                '}';
    }
}
