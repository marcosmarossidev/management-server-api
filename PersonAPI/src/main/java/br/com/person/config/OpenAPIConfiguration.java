package br.com.person.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenAPIConfiguration {

    @Bean
    OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("Person API")
						.version("v1")
						.description("API RESTful")
						.termsOfService("")
						.license(new License()
								.name("Apache 2.0")
								.url("https://www.apache.org/licenses/LICENSE-2.0")));
	}

}
