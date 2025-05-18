package com.aibilling;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class AiBillingSystemApplication {
    public static void main(final String[] args) {
        SpringApplication.run(AiBillingSystemApplication.class, args);
    }
}
