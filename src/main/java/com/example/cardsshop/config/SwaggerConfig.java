package com.example.cardsshop.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Cards Shop API",
        version = "1.0.0",
        description = "API для управления товарами, категориями и пользователями",
        contact = @Contact(
            name = "Служба поддержки",
            email = "support@cardsshop.com"
        )
    ),
    servers = {
        @Server(url = "http://localhost:8080", description = "Локальный сервер разработки")
    }
)
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
            .group("users")
            .pathsToMatch("/api/users/**")
            .build();
    }

    @Bean
    public GroupedOpenApi categoryApi() {
        return GroupedOpenApi.builder()
            .group("categories")
            .pathsToMatch("/api/categories/**")
            .build();
    }

    @Bean
    public GroupedOpenApi productApi() {
        return GroupedOpenApi.builder()
            .group("products")
            .pathsToMatch("/api/products/**")
            .build();
    }
}
