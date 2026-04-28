package com.example.neet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync   // ✅ ADD HERE
public class NeetApplication {

    public static void main(String[] args) {
        SpringApplication.run(NeetApplication.class, args);
    }
}