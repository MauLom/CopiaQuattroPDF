package com.copsis.config;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket apiDocketPrivate() {
		return new Docket(DocumentationType.SWAGGER_2)
				.globalOperationParameters(Arrays.asList(
					new ParameterBuilder().
						name("X-Tenant-Instance")
							.description("DB instance name").modelRef(new ModelRef("string"))
							.parameterType("header")
							.required(false)
						.build(),
					new ParameterBuilder().
						name("X-Tenant-Db")
							.description("DB name").modelRef(new ModelRef("string"))
							.parameterType("header")
							.required(false)
						.build()
					))
				.groupName("Private")
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.copsis.controllers"))
				.paths(PathSelectors.any())
				.build()
				.apiInfo(getApiInfo())
				;
	}
	
	@Bean
	public Docket apiDocketPublic() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("Publics")
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.copsis.controllers.publics"))
				.paths(PathSelectors.any())
				.build()
				.apiInfo(getApiInfo())
				;
	}
	
	private ApiInfo getApiInfo() {
		return new ApiInfo(
				"Quattro PDF API",
				"API Quattro PDF API Description",
				"1.0",
				"https://copsis.com",
				new Contact("Copsis", "https://copsis.com", "hola@copsis.com"),
				"LICENSE",
				"LICENSE URL",
				Collections.emptyList()
				);
	}
}