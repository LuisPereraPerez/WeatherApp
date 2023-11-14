package org.example.control;

import com.google.gson.*;
import org.example.model.Location;
import org.example.model.Weather;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;


public class OpenWeatherMapProvider implements  WeatherProvider{

    private String apiKey;

    public OpenWeatherMapProvider(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public Weather get(Location location){
        try{
            String url = "https://api.openweathermap.org/data/2.5/forecast?lat=" +
                    String.valueOf(location.getLat()) +
                    "&lon=" +
                    String.valueOf(location.getLon()) +
                    "%appid=" +
                    this.apiKey +
                    "units=metric";
            Document doc = Jsoup.connect(url).ignoreContentType(true).get();
            String jsonResponse = doc.text();

            Gson gson =  new Gson();
            JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);
            JsonArray listArray = jsonObject.getAsJsonArray("list");

            Instant targetInstant = findTargetData(listArray);

            if (targetInstant != null){
                for (JsonElement element : listArray){
                    JsonObject data = element.getAsJsonObject();
                    Instant dataInstant = Instant.ofEpochSecond(data.get("dt").getAsLong());

                    if (dataInstant.equals(targetInstant)) {
                        return extractWeatherData(data, location);
                    }
                }
            }

            throw new RuntimeException("No data found for the desired timestamp.");

        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Weather extractWeatherData(JsonObject data, Location location) {
        JsonObject mainData = data.getAsJsonObject("main");
        Double temp = mainData.get("temp").getAsDouble();
        Integer humidity = mainData.get("humidity").getAsInt();
        JsonObject rainData = data.getAsJsonObject("rain");
        Double precipitation = (rainData != null && rainData.has("3h")) ? rainData.get("3h").getAsDouble() : 0.0;
        JsonObject cloudsData = data.getAsJsonObject("clouds");
        Integer cloudiness = cloudsData.get("all").getAsInt();
        JsonObject windData = data.getAsJsonObject("wind");
        Double windSpeed = windData.get("speed").getAsDouble();

        Weather weather = new Weather(temp, precipitation, humidity, cloudiness, windSpeed, Instant.ofEpochSecond(data.get("dt").getAsLong()), location);
        return weather;
    }

    private Instant findTargetData(JsonArray listArray) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime targetDateTime = currentDateTime
                .withHour(12)
                .withMinute(0)
                .withSecond(0)
                .withNano(0)
                .plusDays(1);

        for (JsonElement element : listArray) {
            JsonObject data = element.getAsJsonObject();
            Instant dataInstant = Instant.ofEpochSecond(data.get("dt").getAsLong());
            LocalDateTime dataDateTime = LocalDateTime.ofInstant(dataInstant, ZoneId.of("UTC"));

            if (dataDateTime.isEqual(targetDateTime)){
                return dataInstant;
            }
        }
        return null;
    }
}
