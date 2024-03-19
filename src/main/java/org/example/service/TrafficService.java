package org.example.service;

import org.example.model.Config;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class TrafficService {
    private Config config;
    private final AtomicBoolean sendTraffic = new AtomicBoolean(false);
    private final RestTemplate restTemplate = new RestTemplate();

    public void updateConfig(Config config) {
        this.config = config;
        sendTraffic.set(true);
    }

    public void deleteConfig() {
        sendTraffic.set(false);
    }

    public Config getConfig() {
        return config;
    }

    @Scheduled(fixedDelay = 1000)
    public void sendTraffic() {
        if (sendTraffic.get() && config != null) {
            System.out.println("Sending a request to " + config.getUrl());
        }
    }
}