package org.example.control;

public class EventController {
    private final EventsReceiver receiver;

    public EventController(EventsReceiver eventsReceiver) {
        this.receiver = eventsReceiver;
    }

    public void runTask() throws EventException {
        this.receiver.receive();
    }
}
