package org.example.control;

public class SunriseSunsetException extends Exception{
    public SunriseSunsetException(String message){
        super(message);
    }
    public SunriseSunsetException(String message, Throwable cause) {
        super(message, cause);
    }
}
