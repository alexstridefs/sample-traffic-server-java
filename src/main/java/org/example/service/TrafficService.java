package org.example.service;

import org.example.integration.RemoteEndpoint;
import org.example.model.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.ScheduledExecutorService;


@Service
public class TrafficService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrafficService.class);
    private Config config = null;
    private ScheduledExecutorService scheduler;

    private long lastCallTimeEpochMillis = 0;
    private Thread trafficThread = null;

    public synchronized void updateConfig(Config newConfig) {
        boolean previousConfigPresent = config != null;
        config = newConfig;
        if (previousConfigPresent) {
            // If traffic is already being sent, restart with new config
            stopTraffic();
            startTraffic();
        } else {
            startTraffic();
        }
    }

    public synchronized void deleteConfig() {
        stopTraffic();
        config = null;
    }

    public Config getConfig() {
        return config;
    }

    private void startTraffic() {
        if (config != null) {
            if (config.getCallsPerSecond() > 0) {
                long timeBetweenCalls = 1000 / config.getCallsPerSecond();
                trafficThread = new Thread(() -> {
                    try {
                        while (true) {
                            long currentTime = System.currentTimeMillis();
                            long waitTime = lastCallTimeEpochMillis + timeBetweenCalls - currentTime;
                            if (waitTime > 0) {
                                Thread.sleep(waitTime);
                            }
                            lastCallTimeEpochMillis = System.currentTimeMillis();
                            LOGGER.info("Calling url: " + config.getUrl());
                            RemoteEndpoint.EndpointCallResult result = RemoteEndpoint.call(config.getUrl());
                            if (result.statusCode() != 200) {
                                System.out.println("Status code " + result.statusCode() + " observed!");
                            }
                        }
                    } catch (InterruptedException ignored) {
                    }
                    LOGGER.info("Traffic stopped");

                });
                LOGGER.info("Starting traffic");
                trafficThread.start();
            }
        }
    }

    private void stopTraffic() {
        if (trafficThread != null) {
            trafficThread.interrupt();
            trafficThread = null;
        }
    }
}
