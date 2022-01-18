package com.copsis.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@ConfigurationProperties(prefix = "swagger", ignoreInvalidFields = true, ignoreUnknownFields = true)
@Configuration
public class SwaggerConfig {
	@Value("${spring.profiles.active}")
	private String ambiente;
	
	private static final String BEARER_KEY = "bearer-key";

	private Map<Object, Object> server = new LinkedHashMap<>();

	private List<Server> listServers = new ArrayList<>();

	public Map<Object, Object> getServer() {
		return server;
	}

	public void setServer(Map<String, Map<String, String>> server) {
		server.forEach((key, value) -> {
			Server ser = new Server();
			ser.url(convert(value, "url"));
			ser.description(convert(value, "description"));
			listServers.add(ser);
		});
	}

	public String convert(Map<String, String> source, String prop) {
		return source.get(prop);
	}

	@Bean
	public GroupedOpenApi adminApi() {
		return GroupedOpenApi.builder().group("Private").packagesToScan("com.copsis.controllers").pathsToMatch("/**")
				.build();
	}

	@Bean
	public OpenAPI customOpenAPI() {
		Components components = new Components();
		components.addSecuritySchemes(BEARER_KEY, new SecurityScheme().type(SecurityScheme.Type.HTTP)
				  .description("Bearer Token").scheme("bearer").bearerFormat("JWT"));
		
		return new OpenAPI().servers(listServers).components(components)
				.info(new Info().title("quattro pdf API").description("Apis expuestas").version("v 1.0.0")
						.license(new License().name("Copsis API 1.0").url("https://copsis.com")))
				.externalDocs(new ExternalDocumentation().description("Documentación quattro-pdf")
						.url("https://copsis.com"))
				.security(Arrays.asList(new SecurityRequirement().addList(BEARER_KEY)));
	}
}