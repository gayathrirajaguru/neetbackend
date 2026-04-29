// file: src/main/java/com/example/neet/NeetApplication.java

package com.example.neet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class NeetApplication {

    public static void main(String[] args) {
        SpringApplication.run(NeetApplication.class, args);
    }
}