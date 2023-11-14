package org.example.model;

import java.time.Instant;

public class Weather {
    private final double temp;
    private final double precipitation;
    private final int humidity;
    private final int clouds;
    private final Instant ts;
    private final Location location;

    public Weather(double temp, double precipitation, int humidity, int clouds, Instant ts, Location location) {
        this.temp = temp;
        this.precipitation = precipitation;
        this.humidity = humidity;
        this.clouds = clouds;
        this.ts = ts;
        this.location = location;
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

    public Instant getTs() {
        return ts;
    }

    public Location getLocation() {
        return location;
    }
}
