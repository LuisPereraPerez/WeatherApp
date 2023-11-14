package org.example.control;

import org.example.model.Location;
import org.example.model.Weather;

import java.io.IOException;

public class WeatherController {
    private final WeatherProvider provider;
    private final WeatherStore store;

    public WeatherController(WeatherProvider weatherProvider, WeatherStore weatherStore) {
        this.provider = weatherProvider;
        this.store = weatherStore;
    }

    public WeatherProvider getProvider() {
        return provider;
    }

    public WeatherStore getStore() {
        return store;
    }

    public void runTask() throws IOException {
        Task();
    }

    private void Task() {
        Weather weatherElHierro = this.provider.get(new Location(27.714005, -17.997413, "ElHierro"));
        Weather weatherLaGomera = this.provider.get(new Location(28.089477, -17.112303, "LaGomera"));
        Weather weatherLaPalma = this.provider.get(new Location(28.657405, -17.912805, "LaPalma"));
        Weather weatherTenerife = this.provider.get(new Location(28.482996, -16.317394, "Tenerife"));
        Weather weatherGranCanaria = this.provider.get(new Location(27.998142, -15.419177, "GranCanaria"));
        Weather weatherFuerteventura = this.provider.get(new Location(28.102040, -14.388376, "Fuerteventura"));
        Weather weatherLanzarote = this.provider.get(new Location(29.060357, -13.559169, "Lanzarote"));
        Weather weatherLaGraciosa = this.provider.get(new Location(29.232142, -13.502623, "LaGraciosa"));

        store.storeWeather("ElHierro", weatherElHierro);
        store.storeWeather("LaGomera", weatherLaGomera);
        store.storeWeather("LaPalma", weatherLaPalma);
        store.storeWeather("Tenerife", weatherTenerife);
        store.storeWeather("GranCanaria", weatherGranCanaria);
        store.storeWeather("Fuerteventura", weatherFuerteventura);
        store.storeWeather("Lanzarote", weatherLanzarote);
        store.storeWeather("LaGraciosa", weatherLaGraciosa);
    }
}
