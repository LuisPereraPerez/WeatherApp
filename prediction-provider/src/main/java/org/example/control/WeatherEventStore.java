package org.example.control;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.example.model.Weather;

import javax.jms.*;

public class WeatherEventStore implements WeatherStore{
    private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;
    private static String subject = "topic:prediction.Weather";
    @Override
    public void storeWeather(String location, Weather weather) throws WeatherException{
        Connection connection = null;
        try {
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
            connection = connectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue(subject);
            MessageProducer producer = session.createProducer(destination);
            String serializeData = serializeWeather(weather);
            TextMessage message = session.createTextMessage(serializeData);
            producer.send(message);
            connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
            throw new WeatherException("Error when storing the weather");
        }
    }

    private String serializeWeather(Weather weather) throws WeatherException {
        try {
            return null;
        } catch (Exception e) {
            throw new WeatherException("Error during weather serialization", e);
        }
    }
}
