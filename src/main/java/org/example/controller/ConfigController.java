package org.example.controller;

import org.example.model.Config;
import org.example.service.TrafficService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/config")
public class ConfigController {

    @Autowired
    private TrafficService trafficService;

    @PostMapping
    public void updateConfig(@RequestBody Config config) {
        trafficService.updateConfig(config);
    }

    @DeleteMapping
    public void deleteConfig() {
        trafficService.deleteConfig();
    }

    @GetMapping
    public Config getConfig() {
        return trafficService.getConfig();
    }
}

