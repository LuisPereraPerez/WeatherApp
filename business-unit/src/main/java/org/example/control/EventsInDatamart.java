package org.example.control;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.text.DecimalFormat;
import java.util.*;

public class EventsInDatamart implements EventsReceiver {
    private static String url = ActiveMQConnectionFactory.DEFAULT_BROKER_URL;
    private static String weatherSubject = "prediction.Weather";
    private static String sunriseSunsetSubject = "sunriseSunset.Events";
    private List<String> weatherList = new ArrayList<>();
    private List<String> sunriseSunsetList = new ArrayList<>();

    public void receive() throws EventException {
        Connection connection = null;
        try {
            connection = createConnection();
            connection.setClientID("business-unit");
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            createSubscription(session, weatherSubject, "weather-datalake-builder");
            createSubscription(session, sunriseSunsetSubject, "sunriseSunset-datalake-builder");
        } catch (JMSException e) {
            throw new EventException("Error receiving messages", e);
        }
    }

    private void createSubscription(Session session, String topic, String subscriptionName) throws JMSException {
        Topic destination = session.createTopic(topic);
        MessageConsumer consumer = session.createDurableSubscriber(destination, subscriptionName);
        consumer.setMessageListener(message -> {
            TextMessage textMessage = (TextMessage) message;
            String text = null;
            try {
                text = textMessage.getText();
                saveMessageToList(text);
            } catch (JMSException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private Connection createConnection() throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        return connectionFactory.createConnection();
    }

    private void saveMessageToList(String event) {
        String ss = getSs(event);
        if (ss.equals("OpenWeatherMap")){
            weatherList.add(event);
        } else if (ss.equals("RapidApi")) {
            sunriseSunsetList.add(event);
        }

        if (weatherList.size() >= 64 && sunriseSunsetList.size() >= 32) {
            System.out.println(weatherList);
            System.out.println(sunriseSunsetList);

            List<List<Object>> pairedEvents = pairEventsByDate(weatherList, sunriseSunsetList);
            storeDataInDatabase(pairedEvents);

            weatherList.clear();
            sunriseSunsetList.clear();
        }
    }

    private static String getSs(String event) {
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(event).getAsJsonObject();
        return jsonObject.get("ss").getAsString();
    }

    public static Double getTemp(String event) {
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(event).getAsJsonObject();
        return jsonObject.get("temp").getAsDouble();
    }

    public static Double getPrecipitation(String event) {
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(event).getAsJsonObject();
        return jsonObject.get("precipitation").getAsDouble();
    }

    public static int getHumidity(String event) {
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(event).getAsJsonObject();
        return jsonObject.get("humidity").getAsInt();
    }

    public static int getClouds(String event) {
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(event).getAsJsonObject();
        return jsonObject.get("clouds").getAsInt();
    }

    public static Double getWindSpeed(String event) {
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(event).getAsJsonObject();
        return jsonObject.get("windSpeed").getAsDouble();
    }

    public static String getPredictionTime(String event) {
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(event).getAsJsonObject();
        return jsonObject.get("predictionTime").getAsString();
    }

    private static JsonObject getLocation(String event) {
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(event).getAsJsonObject();
        return jsonObject.get("location").getAsJsonObject();
    }

    private static String getIsland(JsonObject object) {
        return object.get("island").getAsString();
    }

    public static String getSunrise(String event) {
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(event).getAsJsonObject();
        return jsonObject.get("sunrise").getAsString();
    }

    public static String getSunset(String event) {
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(event).getAsJsonObject();
        return jsonObject.get("sunset").getAsString();
    }

    private static List<List<Object>> pairEventsByDate(List<String> weatherList, List<String> sunriseSunsetList) {
        List<List<Object>> resultList = new ArrayList<>();
        Set<String> processedEntries = new HashSet<>();
        for (String weather : weatherList) {
            String dateWeather = extractDate(weather);
            String island = getIsland(getLocation(weather));
            String key = island + dateWeather;
            if (processedEntries.contains(key)) {
                continue;
            }
            List<Object> pair = createEventPair(weather, weatherList, sunriseSunsetList);
            resultList.add(pair);
            processedEntries.add(key);
        }
        return resultList;
    }

    private static List<Object> createEventPair(String baseWeather, List<String> weatherList, List<String> sunriseSunsetList) {
        List<Object> pair = new ArrayList<>();
        pair.add(baseWeather);
        for (String weather : weatherList) {
            if (!weather.equals(baseWeather) && areSameDateAndIsland(baseWeather, weather)) {
                pair.add(weather);
            }
        }
        for (String sunriseSunset : sunriseSunsetList) {
            if (isSameDateAndIsland(baseWeather, sunriseSunset)) {
                pair.add(sunriseSunset);
            }
        }
        pair.add(analyze((String) pair.get(0)));
        pair.add(analyze((String) pair.get(1)));
        pair.add(extractDate((String) pair.get(0)));
        return pair;
    }

    private static boolean areSameDateAndIsland(String weather1, String weather2) {
        String extractedDate = extractDate(weather1);
        String island1 = getIsland(getLocation(weather1));
        String dateWeather2 = extractDate(weather2);
        String island2 = getIsland(getLocation(weather2));
        return !weather1.equals(weather2) && extractedDate.equals(dateWeather2) && island1.equals(island2);
    }

    private static boolean isSameDateAndIsland(String weather, String sunriseSunset) {
        String dateWeather = extractDate(weather);
        String islandWeather = getIsland(getLocation(weather));
        String dateSunrise = getSunrise(sunriseSunset).substring(0, 10);
        String islandSunriseSunset = getIsland(getLocation(sunriseSunset));
        return dateWeather.equals(dateSunrise) && islandWeather.equals(islandSunriseSunset);
    }

    private static String analyze(String weather) {
        Double eval = getTemp(weather) * 0.15 + getPrecipitation(weather) * 0.5 + getHumidity(weather) * 0.1 + getWindSpeed(weather) * 0.15 + getClouds(weather) * 0.3;
        if (eval >= 30){
            return "Not recommended";
        }else if (eval < 30 && eval >= 20) {
            return "Not bad, but could be better";
        } else if (eval < 20 && eval >= 10) {
            return "It's ok";
        } else if (eval < 10) {
            return "It's perfect";
        }
        return String.valueOf(eval);
    }

    private static String extractDate(String weather) {
        String dateWeather = getPredictionTime(weather);
        return dateWeather.substring(0, 10);
    }

    private static void storeDataInDatabase(List<List<Object>> pairedEvents) {
        SQLiteStore sqLiteStore = new SQLiteStore();
        for (List<Object> objects : pairedEvents) {
            String weather1 = (String) objects.get(0);
            String weather2 = (String) objects.get(1);
            String sunriseSunset = (String) objects.get(2);
            String evalSunrise = (String) objects.get(3);
            String evalSunset = (String) objects.get(4);
            String date = (String) objects.get(5);

            String islandName = getIsland(getLocation(weather1));
            sqLiteStore.store(islandName, weather2, sunriseSunset, evalSunset, date, "sunset");
            sqLiteStore.store(islandName, weather1, sunriseSunset, evalSunrise, date, "sunrise");
        }
    }
}
