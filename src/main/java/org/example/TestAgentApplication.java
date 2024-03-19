package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class TestAgentApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestAgentApplication.class, args);
    }
}