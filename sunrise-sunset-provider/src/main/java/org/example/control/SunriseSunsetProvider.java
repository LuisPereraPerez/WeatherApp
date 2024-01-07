package org.example.control;

import org.example.model.Location;
import org.example.model.SunriseSunset;

import java.util.List;

public interface SunriseSunsetProvider {
    List<SunriseSunset> get(Location location);
}
