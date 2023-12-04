package org.example;

public class EventController {
    private final EventsReceiver receiver;

    public EventController(EventsReceiver eventsReceiver) {
        this.receiver = eventsReceiver;
    }

    public void runTask() throws WeatherException {
        this.receiver.receive();
    }
}
