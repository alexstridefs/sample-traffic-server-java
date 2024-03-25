package org.example.service;

import org.example.integration.RemoteEndpoint;
import org.example.model.Config;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Service
public class TrafficService {
    private Config config = null;
    private ScheduledExecutorService scheduler;

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
                scheduler = Executors.newSingleThreadScheduledExecutor();
                scheduler.scheduleAtFixedRate(() -> {
                    System.out.println("Sending a request to " + config.getUrl());
                    RemoteEndpoint.EndpointCallResult result = RemoteEndpoint.call(config.getUrl());
                    if (result.statusCode() != 200) {
                        System.out.println("Status code " + result.statusCode() + " observed!");
                    }
                }, 0, 1000 / config.getCallsPerSecond(), TimeUnit.MILLISECONDS);
            }
        }
    }

    private void stopTraffic() {
        if (scheduler != null) {
            scheduler.shutdownNow();
            scheduler = null;
        }
    }
}
