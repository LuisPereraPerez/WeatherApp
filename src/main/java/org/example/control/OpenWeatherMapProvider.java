package org.example.control;

import org.example.model.Location;
import org.example.model.Weather;

public class OpenWeatherMapProvider implements  WeatherProvider{
    public OpenWeatherMapProvider(String apiKey) {
    }

    @Override
    public Weather get(Location location){
        return null;
    }
}
