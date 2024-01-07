package org.example.control;

import java.sql.*;
import java.util.Arrays;
import java.util.List;

import static org.example.control.EventsInDatamart.*;

public class SQLiteStore {
    private static final String DATABASE_URL = "jdbc:sqlite:datamart.db";
    private static final List<String> ISLANDS = Arrays.asList("ElHierro", "LaGomera", "LaPalma", "Tenerife", "GranCanaria", "Fuerteventura", "Lanzarote", "LaGraciosa");

    public SQLiteStore() {
        initializeDatabase();
    }

    private void initializeDatabase() {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL)) {
            try (Statement statement = connection.createStatement()) {
                for (String island : ISLANDS) {
                    String tableName = island + "_sunrise";
                    String createTableQuery = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                            "temp REAL, " +
                            "precipitation REAL, " +
                            "humidity INTEGER, " +
                            "clouds INTEGER, " +
                            "windSpeed REAL, " +
                            "predictionTime TEXT, " +
                            "sunrise TEXT, " +
                            "eval TEXT, " +
                            "ts TEXT PRIMARY KEY)";
                    statement.executeUpdate(createTableQuery);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void store(String island, String weather, String sunriseSunset, String eval, String date, String type) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL)){
            String tableName = "";
            String insert = "";
            if (type.equals("sunrise")) {
                tableName = island + "_sunrise";
                insert = " (temp, precipitation, humidity, clouds, windSpeed, predictionTime, sunrise, eval, ts) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
            }
            else if (type.equals("sunset")) {
                tableName = island + "_sunset";
                insert = " (temp, precipitation, humidity, clouds, windSpeed, predictionTime, sunset, eval, ts) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
            }
            String insertQuery = "INSERT OR REPLACE INTO " + tableName + insert;
            try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
                statement.setDouble(1, getTemp(weather));
                statement.setDouble(2, getPrecipitation(weather));
                statement.setInt(3, getHumidity(weather));
                statement.setInt(4, getClouds(weather));
                statement.setDouble(5, getWindSpeed(weather));
                statement.setString(6, getPredictionTime(weather));
                if (type.equals("sunrise")){
                    statement.setString(7, getSunrise(sunriseSunset));
                } else if (type.equals("sunset")) {
                    statement.setString(7, getSunset(sunriseSunset));
                }
                statement.setString(8, eval);
                statement.setString(9, date);
                statement.executeUpdate();
            }
        }
        catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
