package com.proma.patientmanagement.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Metadata shown on the Swagger UI page at /swagger-ui.html.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI patientManagementOpenApi() {
        return new OpenAPI().info(new Info()
                .title("Patient Management API")
                .description("A Spring Boot REST API for managing patient records")
                .version("1.0.0")
                .contact(new Contact().name("Proma")));
    }
}
