package com.carbonai.cbam.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configures Swagger / OpenAPI documentation for the MVP backend.
 *
 * Beginner-friendly explanation:
 * Swagger generates a browser-based page that lets developers see the API
 * endpoints, read descriptions, and test requests without writing code first.
 */
@Configuration
public class OpenApiConfig {

    /**
     * Creates the OpenAPI metadata object shown in Swagger UI.
     */
    @Bean
    public OpenAPI carbonAiOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("CarbonAI TR - CBAM Calculator Engine")
                        .description("Deterministic Spring Boot backend for CBAM emissions, certificate, cost, scenario, and validation calculations.")
                        .version("v1")
                        .contact(new Contact().name("CarbonAI TR"))
                        .license(new License().name("Internal MVP")));
    }
}
