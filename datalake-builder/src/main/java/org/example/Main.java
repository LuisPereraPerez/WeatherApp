package org.example;

import org.example.control.*;

import java.util.Timer;
import java.util.TimerTask;

public class Main {
    public static void main(String[] args) throws EventException {
        EventsReceiver eventsReceiver = new EventStoreBuilder();
        EventController eventController = new EventController(eventsReceiver);
        eventController.runTask();
    }
}
