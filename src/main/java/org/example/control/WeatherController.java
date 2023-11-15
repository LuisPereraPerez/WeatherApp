package org.example.control;

import org.example.model.Location;
import org.example.model.Weather;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class WeatherController {
    private final WeatherProvider provider;
    private final WeatherStore store;

    public WeatherController(WeatherProvider weatherProvider, WeatherStore weatherStore) {
        this.provider = weatherProvider;
        this.store = weatherStore;
    }

    public void runTask() throws IOException {
        task();
    }

    private void task() {
        List<Location> locations = Arrays.asList(
                new Location(27.714005, -17.997413, "ElHierro"),
                new Location(28.089477, -17.112303, "LaGomera"),
                new Location(28.657405, -17.912805, "LaPalma"),
                new Location(28.482996, -16.317394, "Tenerife"),
                new Location(27.998142, -15.419177, "GranCanaria"),
                new Location(28.102040, -14.388376, "Fuerteventura"),
                new Location(29.060357, -13.559169, "Lanzarote"),
                new Location(29.232142, -13.502623, "LaGraciosa")
        );

        for (Location location : locations) {
            List<Weather> weatherList = provider.get(location);
            if (weatherList != null && !weatherList.isEmpty()) {
                for (Weather weather : weatherList) {
                    store.storeWeather(weather.getLocation().getIsland(), weather);
                }
            }
        }
    }
}
