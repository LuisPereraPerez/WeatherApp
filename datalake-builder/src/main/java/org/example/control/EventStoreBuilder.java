package org.example.control;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EventStoreBuilder implements EventsReceiver {
    private static String url = ActiveMQConnectionFactory.DEFAULT_BROKER_URL;
    private static String weatherSubject  = "prediction.Weather";
    private static String sunriseSunsetSubject = "sunriseSunset.Events";
    private static String directory = "DataLake";

    public void receive() throws EventException {
        Connection connection = null;
        try {
            connection = createConnection();
            connection.setClientID("datalake-builder");
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
            } catch (JMSException e) {
                throw new RuntimeException(e);
            }
            try {
                storeEvent(text);



            } catch (EventException e){
                throw new RuntimeException(e);
            }
        });
    }

    private Connection createConnection() throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        return connectionFactory.createConnection();
    }

    private void storeEvent(String event) throws EventException {
        try {
            String ss = getSs(event);
            Date timestamp = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            String dateStr = dateFormat.format(timestamp);
            String sourceDirectory = directory + File.separator + ss;
            createDirectories(sourceDirectory);
            String fileName = sourceDirectory + File.separator + dateStr + ".events";
            appendToFile(fileName, event);
        } catch (IOException e) {
            throw new EventException("Error storing event", e);
        }
    }

    private String getSs(String event) {
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(event).getAsJsonObject();
        return jsonObject.get("ss").getAsString();
    }


    private void createDirectories(String sourceDirectory) throws EventException {
        File sourceDirectoryFile = new File(sourceDirectory);
        if (!sourceDirectoryFile.exists() && !sourceDirectoryFile.mkdirs()) {
            throw new EventException("Failed to create source directory: " + sourceDirectory);
        }
    }

    private void appendToFile(String filePath, String event) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            writeToFile(filePath, event);
        } else {
            try (FileWriter writer = new FileWriter(filePath, true)) {
                writer.write(event + "\n");
            }
        }
    }

    private void writeToFile(String filePath, String event) throws IOException {
        try (FileWriter writer = new FileWriter(filePath, true)) {
            writer.write(event + "\n");
        }
    }
}
