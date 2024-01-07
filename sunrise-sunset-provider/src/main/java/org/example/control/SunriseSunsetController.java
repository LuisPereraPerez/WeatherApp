package org.example.control;

import org.example.model.Location;
import org.example.model.SunriseSunset;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class SunriseSunsetController {
    private final SunriseSunsetProvider provider;
    private final SunriseSunsetStore store;

    public SunriseSunsetController(SunriseSunsetProvider provider, SunriseSunsetStore store) {
        this.provider = provider;
        this.store = store;
    }

    public void runTask() throws IOException, SunriseSunsetException {
        List<Location> locations = createLocations();
        processData(locations);
    }


    private static List<Location> createLocations() {
        return Arrays.asList(
                new Location(27.714005, -17.997413, "ElHierro"),
                new Location(28.089477, -17.112303, "LaGomera"),
                new Location(28.657405, -17.912805, "LaPalma"),
                new Location(28.482996, -16.317394, "Tenerife"),
                new Location(27.998142, -15.419177, "GranCanaria"),
                new Location(28.102040, -14.388376, "Fuerteventura"),
                new Location(29.060357, -13.559169, "Lanzarote"),
                new Location(29.232142, -13.502623, "LaGraciosa")
        );
    }

    private void processData(List<Location> locations) throws SunriseSunsetException {
        for (Location location : locations) {
            List<SunriseSunset> sunriseSunsetList = provider.get(location);
            storeData(sunriseSunsetList, location.getIsland());
        }
    }

    private void storeData(List<SunriseSunset> sunriseSunsetList, String island) throws SunriseSunsetException {
        if (sunriseSunsetList != null && !sunriseSunsetList.isEmpty()) {
            for (SunriseSunset sunriseSunset : sunriseSunsetList) {
                store.store(island, sunriseSunset);
            }
        }
    }
}
