package com.pios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PiosApplication {
    public static void main(String[] args) {
        SpringApplication.run(PiosApplication.class, args);
    }
}
