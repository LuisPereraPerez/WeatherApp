package org.example;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EventStoreBuilder implements EventsReceiver {
    private static String url = ActiveMQConnectionFactory.DEFAULT_BROKER_URL;
    private static String subject = "topic:prediction.Weather";
    private static String directory = "eventstore/prediction.Weather";

    public void receive() throws WeatherException {
        Connection connection = null;
        try {
            connection = createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageConsumer consumer = session.createConsumer(createQueue(session));
            connection.start();

            while (true) {
                Message message = consumer.receive();
                if (message instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage) message;
                    storeEvent(textMessage.getText());
                }
            }
        } catch (JMSException e) {
            throw new WeatherException("Error receiving messages", e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Connection createConnection() throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        return connectionFactory.createConnection();
    }

    private Destination createQueue(Session session) throws JMSException {
        return session.createQueue(subject);
    }

    private void storeEvent(String event) throws WeatherException {
        try {
            String ss = "prediction-provider";

            Date timestamp = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

            String dateStr = dateFormat.format(timestamp);

            String sourceDirectory = directory + File.separator + ss;
            String filePath = sourceDirectory + File.separator + dateStr + ".events";

            File sourceDirectoryFile = new File(sourceDirectory);
            if (!sourceDirectoryFile.exists()) {
                if (!sourceDirectoryFile.mkdirs()) {
                    throw new WeatherException("Failed to create source directory: " + sourceDirectory);
                }
            }

            try (FileWriter writer = new FileWriter(filePath, true)) {
                writer.write(event + "\n");
            }
        } catch (IOException e) {
            throw new WeatherException("Error storing event", e);
        }
    }

}
