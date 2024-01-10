package org.example;

import org.example.control.*;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class Main {
    public static void main(String[] args) throws SunriseSunsetException, IOException {
        validateArguments(args);

        String apiKey = args[0];

        SunriseSunsetProvider provider = new SunriseSunsetApi(apiKey);
        SunriseSunsetStore store = new SunriseSunsetEventStore();
        SunriseSunsetController controller = new SunriseSunsetController(provider, store);

        scheduleTask(controller);
    }

    private static void validateArguments(String[] args) {
        if (args.length != 1) {
            System.err.println("Please provide the API Key as an argument.");
            System.exit(1);
        }
    }

    private static void scheduleTask(SunriseSunsetController controller) {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    try {
                        controller.runTask();
                    } catch (SunriseSunsetException e) {
                        throw new RuntimeException(e);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }, 0, 6 * 60 * 60 * 1000);
    }
}
