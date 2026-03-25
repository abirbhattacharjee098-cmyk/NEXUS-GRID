package com.nexus.master;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MasterNodeApplication {
    public static void main(String[] args) {
        // Run Master Node
        SpringApplication.run(MasterNodeApplication.class, args);
    }
}
