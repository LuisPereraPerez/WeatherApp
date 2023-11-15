package org.example;

import org.example.control.*;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1){
            System.err.println("Please provide the API Key as an argument.");
            System.exit(1);
        }

        String apiKey = args[0];

        WeatherProvider weatherProvider = new OpenWeatherMapProvider(apiKey);
        WeatherStore weatherStore = new SQLiteWeatherStore();
        WeatherController weatherController = new WeatherController(weatherProvider, weatherStore);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    weatherController.runTask();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }, 0, 6 * 60 * 60 * 1000);
    }
}