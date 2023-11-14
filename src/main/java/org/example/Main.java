package org.example;

import org.example.control.*;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length != 1){
            System.err.println("Please provide the API Key as an argument.");
            System.exit(1);
        }

        String apiKey = args[0];

        WeatherProvider weatherProvider = new OpenWeatherMapProvider(apiKey);
        WeatherStore weatherStore = new SQLiteWeatherStore();
        WeatherController weatherController = new WeatherController(weatherProvider, weatherStore);

        weatherController.runTask();
    }
}