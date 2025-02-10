package com.cardshop.app.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Cards Shop API",
        version = "1.0.0",
        description = "API для управления товарами, корзиной и пользователями",
        contact = @Contact(
            name = "Служба поддержки",
            email = "support@cardshop.com"
        )
    ),
    servers = {
        @Server(url = "http://localhost:8080", description = "Локальный сервер"),
        @Server(url = "https://cardshop.com/api", description = "Продакшн сервер")
    }
)
public class SwaggerConfig {
    // Конфигурация Swagger/OpenAPI
}
