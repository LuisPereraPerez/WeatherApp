package org.example.model;

import com.google.gson.JsonObject;

import java.time.Instant;

public class SunriseSunset {
    private final String sunrise;
    private final String sunset;
    private final Location location;
    private Instant ts;
    private String ss;

    public SunriseSunset(String sunrise, String sunset, Location location, Instant ts, String ss) {
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.location = location;
        this.ts = ts;
        this.ss = ss;
    }

    public String getSunrise() {
        return sunrise;
    }

    public String getSunset() {
        return sunset;
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
        return "SunriseSunset{" +
                "sunrise='" + sunrise + '\'' +
                ", sunset='" + sunset + '\'' +
                ", location=" + location +
                ", ts=" + ts +
                ", ss='" + ss + '\'' +
                '}';
    }

    public static SunriseSunset processSunriseSunsetData(JsonObject jsonObject, Location location) {
        String sunriseTime = jsonObject.getAsJsonPrimitive("sunrise").getAsString();
        String sunsetTime = jsonObject.getAsJsonPrimitive("sunset").getAsString();
        return new SunriseSunset(sunriseTime, sunsetTime, location, Instant.now(), "RapidApi");
    }
}
