package org.example.control;

import com.google.gson.*;
import org.example.model.Location;
import org.example.model.Weather;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class OpenWeatherMapProvider implements WeatherProvider {

    private String apiKey;

    public OpenWeatherMapProvider(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public List<Weather> get(Location location) {
        try {
            String url = "https://api.openweathermap.org/data/2.5/forecast?lat=" +
                    String.valueOf(location.getLat()) +
                    "&lon=" +
                    String.valueOf(location.getLon()) +
                    "&appid=" +
                    this.apiKey +
                    "&units=metric";

            Document doc = Jsoup.connect(url).ignoreContentType(true).get();
            String jsonResponse = doc.text();

            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);
            JsonArray listArray = jsonObject.getAsJsonArray("list");

            List<Weather> fiveDays = new ArrayList<>();

            for (int i = 0; i < listArray.size(); i++) {
                JsonObject data = listArray.get(i).getAsJsonObject();

                if (isPrediction(data)) {
                    Weather weather = extractWeatherData(data, location);
                    fiveDays.add(weather);

                    if (fiveDays.size() == 5) {
                        break;
                    }
                }
            }
            return fiveDays;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isPrediction(JsonObject data) {
        String predictionDateTime = data.get("dt_txt").getAsString();

        predictionDateTime = predictionDateTime.replace("Z", "");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(predictionDateTime, formatter);

        return localDateTime.getHour() == 12 && localDateTime.getMinute() == 0;
    }

    private Weather extractWeatherData(JsonObject data, Location location) {
        JsonObject mainData = data.getAsJsonObject("main");
        Double temperature = mainData.get("temp").getAsDouble();

        Integer humidity = mainData.get("humidity").getAsInt();

        JsonObject rainData = data.getAsJsonObject("rain");
        Double possibilityOfPrecipitation = (rainData != null && rainData.has("3h")) ? rainData.get("3h").getAsDouble() : 0.0;

        JsonObject cloudsData = data.getAsJsonObject("clouds");
        Integer cloudiness = cloudsData.get("all").getAsInt();

        JsonObject windData = data.getAsJsonObject("wind");
        Double windSpeed = windData.get("speed").getAsDouble();

        Weather weather = new Weather(temperature, possibilityOfPrecipitation, humidity, cloudiness, windSpeed, Instant.ofEpochSecond(data.get("dt").getAsLong()), location);
        return weather;
    }
}
