package org.example.control;

import org.example.model.Weather;

public interface WeatherStore {
    void storeWeather(String location, Weather weather);
}
