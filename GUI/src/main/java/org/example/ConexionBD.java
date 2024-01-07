package org.example;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ConexionBD {
    public ConexionBD() {
    }

    public static List<List<String>> ConexionBD(String tableName, String type) {
        List<List<String>> list = new ArrayList<>();
        Connection connection = null;
        try {
            Class.forName("org.sqlite.JDBC");

            String path = getPath("datamart.db");
            String url = "jdbc:sqlite:" + path;
            System.out.println("Database path: " + url);
            connection = DriverManager.getConnection(url);

            System.out.println("Successful database connection.");

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName);
            while (resultSet.next()) {
                List<String> data = new ArrayList<>();
                Double temp = resultSet.getDouble("temp");
                Double precipitation = resultSet.getDouble("precipitation");
                Integer humidity = resultSet.getInt("humidity");
                Integer clouds = resultSet.getInt("clouds");
                Double windSpeed = resultSet.getDouble("windSpeed");
                String predictionTime = resultSet.getString("predictionTime").substring(11, 16);
                String sunriseOrSunset = "";
                if (type.equals("sunrise")) {
                    sunriseOrSunset = resultSet.getString("sunrise").substring(11, 16);
                }
                if (type.equals("sunset")) {
                    sunriseOrSunset = resultSet.getString("sunset").substring(11, 16);
                }
                String eval = resultSet.getString("eval");
                String tsString  = resultSet.getString("ts");
                LocalDate ts = LocalDate.parse(tsString);
                if (!ts.isBefore(LocalDate.now())) {
                    data.add(String.valueOf(temp));
                    data.add(String.valueOf(precipitation));
                    data.add(String.valueOf(humidity));
                    data.add(String.valueOf(clouds));
                    data.add(String.valueOf(windSpeed));
                    data.add(predictionTime);
                    data.add(sunriseOrSunset);
                    data.add(eval);
                    data.add(tsString);
                    list.add(data);
                }
            }
            return list;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                    System.out.println("Closed connection.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public static String getPath(String filename){
        String workingDirectory = System.getProperty("user.dir");
        String parentDirectory = getParentDirectory(workingDirectory);
        return parentDirectory + "\\" + filename;
    }

    public static String getParentDirectory(String directory) {
        File file = new File(directory);
        File parentFile = file.getParentFile();
        if (parentFile != null) {
            String parentDirectory = parentFile.getAbsolutePath();
            return parentDirectory;
        } else {
            return null;
        }
    }
}
