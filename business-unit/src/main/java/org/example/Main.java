package org.example;

import org.example.control.*;

public class Main {
    public static void main(String[] args) throws EventException {
        EventsInDatamart eventsReceiver = new EventsInDatamart();
        EventsController eventController = new EventsController(eventsReceiver);
        eventController.runTask();
    }
}
