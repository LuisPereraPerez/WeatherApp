package org.example;

import org.example.control.*;

public class Main {
    public static void main(String[] args){
        EventsReceiver eventsReceiver = new EventStoreBuilder();
        EventController eventController = new EventController(eventsReceiver);
        try {
            eventController.runTask();
        } catch (WeatherException e) {
            throw new RuntimeException(e);
        }
    }
}
