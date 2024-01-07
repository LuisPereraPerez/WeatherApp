package org.example.control;

public class EventsController {
    private final EventsReceiver receiver;

    public EventsController  (EventsReceiver eventsReceiver) {
        this.receiver = eventsReceiver;
    }

    public void runTask() throws EventException {
        this.receiver.receive();
    }
}
