package org.example.control;

import org.example.model.Weather;

import java.sql.*;
import java.util.Arrays;
import java.util.List;

public class SQLiteWeatherStore implements  WeatherStore{
    private static final String DATABASE_URL = "jdbc:sqlite:weather.db";

    private static final List<String> ISLANDS = Arrays.asList("ElHierro", "LaGomera", "LaPalma", "Tenerife", "GranCanaria", "Fuerteventura", "Lanzarote", "LaGraciosa");

    public SQLiteWeatherStore(){
        initializeDatabase();
    }

    private void initializeDatabase() {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL)) {
            try (Statement statement = connection.createStatement()) {
                for (String island : ISLANDS) {
                    String dropTableQuery = "DROP TABLE IF EXISTS " + island;
                    statement.executeUpdate(dropTableQuery);
                }
                for (String island : ISLANDS) {
                    String createTableQuery = "CREATE TABLE " + island + " (" +
                            "temp REAL, " +
                            "precipitation REAL, " +
                            "humidity INTEGER, " +
                            "clouds INTEGER, " +
                            "windSpeed REAL, " +
                            "ts TEXT)";
                    statement.executeUpdate(createTableQuery);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void storeWeather(String location, Weather weather) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL)){
            String insertQuery = "INSERT OR REPLACE INTO " + weather.getLocation().getIsland() +
                    " (temp, precipitation, humidity, clouds, windSpeed, ts) VALUES(?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
                statement.setDouble(1, weather.getTemp());
                statement.setDouble(2, weather.getPrecipitation());
                statement.setInt(3, weather.getHumidity());
                statement.setInt(4, weather.getClouds());
                statement.setDouble(5, weather.getWindSpeed());
                statement.setString(6, weather.getTs().toString());

                statement.executeUpdate();
            }
        }
        catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
