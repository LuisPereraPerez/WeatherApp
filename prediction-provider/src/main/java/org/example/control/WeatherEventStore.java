package org.example.control;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.example.model.Weather;

import javax.jms.*;
import java.time.Instant;

public class WeatherEventStore implements WeatherStore{
    private static final String url = ActiveMQConnection.DEFAULT_BROKER_URL;
    private static final String subject = "prediction.Weather";
    private final Gson gson;

    public WeatherEventStore() {
        gson = new GsonBuilder()
                .registerTypeAdapter(Instant.class, new InstantTypeAdapter())
                .create();
    }
    @Override
    public void storeWeather(String location, Weather weather) throws WeatherException{
        Connection connection = null;
        try {
            connection = createConnection();
            Session session = createSession(connection);
            Destination destination = createTopic(session);
            MessageProducer producer = createProducer(session, destination);
            String serializedData = serializeWeather(weather);
            TextMessage message = createTextMessage(session, serializedData);
            producer.send(message);
        } catch (JMSException e) {
            e.printStackTrace();
            throw new WeatherException("Error when storing the weather", e);
        }
    }

    private Connection createConnection() throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        return connectionFactory.createConnection();
    }

    private Session createSession(Connection connection) throws JMSException {
        return connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }

    private Destination createTopic(Session session) throws JMSException {
        return session.createTopic(subject);
    }

    private MessageProducer createProducer(Session session, Destination destination) throws JMSException {
        return session.createProducer(destination);
    }

    private TextMessage createTextMessage(Session session, String serializedData) throws JMSException {
        return session.createTextMessage(serializedData);
    }

    private String serializeWeather(Weather weather) throws WeatherException {
        try {
            return gson.toJson(weather);
        } catch (Exception e) {
            throw new WeatherException("Error during weather serialization", e);
        }
    }
}
