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
            for (String island : ISLANDS) {
                String createTableQuery = "CREATE TABLE IF NOT EXISTS " + island + " (" +
                        "temp REAL, " +
                        "precipitation REAL, " +
                        "humidity INTENGER, " +
                        "clouds INTENGER, " +
                        "windSpeed REAL, " +
                        "ts TEXT)";
                PreparedStatement statement = connection.prepareStatement(createTableQuery);
                statement.executeUpdate();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void storeWeather(String location, Weather weather) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL)){
            String insertQuery = "INSERT OR REPLACE INTO " + weather.getLocation().getIsland() + " (temp, precipitation, humidity, clouds, windSpeed, ts) VALUES(?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(insertQuery);
            statement.setDouble(1, weather.getTemp());
            statement.setDouble(2, weather.getPrecipitation());
            statement.setInt(3, weather.getHumidity());
            statement.setInt(4, weather.getClouds());
            statement.setDouble(5, weather.getWindSpeed());
            statement.setString(6, weather.getTs().toString());
            statement.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
