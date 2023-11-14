package org.example.control;

import org.example.model.Location;
import org.example.model.Weather;

public interface WeatherProvider {
    Weather get(Location location);
}
