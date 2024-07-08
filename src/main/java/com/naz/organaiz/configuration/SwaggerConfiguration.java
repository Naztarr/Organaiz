package com.naz.organaiz.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Organaiz - HNG project")
                                .version("1.0")
                                .description("HNG backend track project")
                                .contact(
                                        new Contact()
                                                .name("Chinaza Herbert")
                                                .url("https://github.com/Naztarr")
                                )
                                .license(
                                        new License()
                                                .name("Apache 2.4")
                                                .url("https://github.com/Naztarr")
                                )
                ).components(
                        new Components()
                                .addSecuritySchemes(
                                        "Bearer Authentication",
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("Bearer")
                                                .bearerFormat("JWT")
                                )
                )
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .externalDocs(
                        new ExternalDocumentation()
                                .description("Organaiz REST API Documentation")
                                .url("https://github.com/Naztarr")
                );
    }
}
