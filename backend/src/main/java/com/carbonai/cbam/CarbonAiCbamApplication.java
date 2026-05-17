package com.carbonai.cbam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the CarbonAI TR CBAM calculator backend.
 *
 * Beginner-friendly explanation:
 * This is the main class that starts the Spring Boot application. When the
 * application runs, Spring scans the project, creates the controller and
 * service beans, seeds the demo data, and exposes the REST endpoints.
 *
 * This application exposes deterministic REST APIs for:
 * - default emissions calculations
 * - actual emissions calculations
 * - simple cost estimation
 * - advanced certificate estimation
 * - scenario analysis
 * - report validation
 *
 * No AI or external registry integrations are used here.
 */
@SpringBootApplication
public class CarbonAiCbamApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarbonAiCbamApplication.class, args);
    }
}
