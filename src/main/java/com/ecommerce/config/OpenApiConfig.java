package com.ecommerce.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .components(new Components()
                .addSecuritySchemes("Bearer Authentication", new SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")
                    .description("JWT Bearer token for API authentication")
                )
            )
            .info(new Info()
                .title("E-Commerce Platform API")
                .description("Spring Boot 3.x E-Commerce Backend API\n\n" +
                    "## Architecture Overview\n" +
                    "- **Language**: Java 21\n" +
                    "- **Framework**: Spring Boot 3.x\n" +
                    "- **Database**: PostgreSQL\n" +
                    "- **Cache**: Redis\n" +
                    "- **Search**: Meilisearch\n" +
                    "- **Security**: JWT + Spring Security\n\n" +
                    "## API Categories\n" +
                    "- **Products**: Full CRUD operations for products\n" +
                    "- **Categories**: Product category management\n" +
                    "- **Brands**: Brand management\n" +
                    "- **Search**: Full-text search with Meilisearch\n" +
                    "- **Auth**: JWT-based authentication\n" +
                    "- **Admin**: Administrative operations")
                .version("1.0.0")
                .contact(new Contact()
                    .name("API Support")
                    .email("support@ecommerce.com")
                    .url("https://ecommerce.com")
                )
                .license(new License()
                    .name("Apache 2.0")
                    .url("https://www.apache.org/licenses/LICENSE-2.0.html")
                )
            );
    }
}
