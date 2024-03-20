package org.example.service;

import org.example.model.Config;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class TrafficService {
    private Config config;
    private final AtomicBoolean sendTraffic = new AtomicBoolean(false);
    private final RestTemplate restTemplate = new RestTemplate();
    private ScheduledExecutorService scheduler;

    public synchronized void updateConfig(Config config) {
        this.config = config;
        if (sendTraffic.getAndSet(true)) {
            // If traffic is already being sent, restart with new config
            stopTraffic();
            startTraffic();
        } else {
            startTraffic();
        }
    }

    public synchronized void deleteConfig() {
        stopTraffic();
        sendTraffic.set(false);
        config = null;
    }

    public Config getConfig() {
        return config;
    }

    private void startTraffic() {
        if (config != null && config.getRate() > 0) {
            scheduler = Executors.newSingleThreadScheduledExecutor();
            scheduler.scheduleAtFixedRate(() -> {
                System.out.println("Sending a request to " + config.getUrl());
                // restTemplate.getForObject(config.getUrl(), String.class); Uncomment to send actual request
            }, 0, 1, TimeUnit.SECONDS); // Adjust the third argument based on your rate logic
        }
    }

    private void stopTraffic() {
        if (scheduler != null) {
            scheduler.shutdownNow();
            scheduler = null;
        }
    }
}
