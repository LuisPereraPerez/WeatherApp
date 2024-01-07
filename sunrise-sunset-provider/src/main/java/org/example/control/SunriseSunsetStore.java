package org.example.control;

import org.example.model.SunriseSunset;

public interface SunriseSunsetStore {
    void store(String location, SunriseSunset sunriseSunset) throws SunriseSunsetException;
}
