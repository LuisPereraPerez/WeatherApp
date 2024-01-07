package org.example.control;

public class EventException extends Exception{
    public EventException(String message){
        super(message);
    }
    public EventException(String message, Throwable cause) {
        super(message, cause);
    }
}
