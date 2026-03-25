package com.nexus.worker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WorkerNodeApplication {
    public static void main(String[] args) {
        // Run Worker Node
        SpringApplication.run(WorkerNodeApplication.class, args);
    }
}
