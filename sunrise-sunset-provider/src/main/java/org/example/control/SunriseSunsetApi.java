package org.example.control;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.example.model.Location;
import org.example.model.SunriseSunset;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SunriseSunsetApi implements  SunriseSunsetProvider {
    private static String apiKey;

    public SunriseSunsetApi(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public List<SunriseSunset> get(Location location) {
        try {
            return getSunriseSunsetData(location);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<SunriseSunset> getSunriseSunsetData(Location location) throws IOException, InterruptedException {
        List<LocalDate> dateList = createDates();
        List<SunriseSunset> sunriseSunsetData = new ArrayList<>();
        for (LocalDate date: dateList){
            String url = buildApiUrl(location, date);
            JsonObject jsonObject = fetchDataFromApi(url);
            SunriseSunset sunriseSunset = SunriseSunset.processSunriseSunsetData(jsonObject, location);
            Thread.sleep(1000);
            sunriseSunsetData.add(sunriseSunset);
        }
        return sunriseSunsetData;
    }

    private static String buildApiUrl(Location location, LocalDate currentDate) {
        return "https://sunrise-sunset-times.p.rapidapi.com/getSunriseAndSunset?date=" +
                String.valueOf(currentDate) +
                "&latitude=" +
                String.valueOf(location.getLat()) +
                "&longitude=" +
                String.valueOf(location.getLon());
    }

    private static JsonObject fetchDataFromApi(String url) throws IOException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("X-RapidAPI-Key", apiKey)
                .header("X-RapidAPI-Host", "sunrise-sunset-times.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String jsonResponse = response.body();
            return parseJsonResponse(jsonResponse);
        } catch (InterruptedException e) {
            throw new IOException("Error en la llamada HTTP", e);
        }
    }

    private static JsonObject parseJsonResponse(String jsonResponse) {
        JsonParser jsonParser = new JsonParser();
        return jsonParser.parse(jsonResponse).getAsJsonObject();
    }
    private static List<LocalDate> createDates() {
        List<LocalDate> fiveDays = new ArrayList<>();
        LocalDate currentDay = LocalDate.now();
        LocalDate nextDay = currentDay.plusDays(1);
        fiveDays.add(nextDay);
        for (int i = 1; i <= 3; i++) {
            LocalDate nextDay1 = nextDay.plusDays(i);
            fiveDays.add(nextDay1);
        }
        return fiveDays;
    }
}
