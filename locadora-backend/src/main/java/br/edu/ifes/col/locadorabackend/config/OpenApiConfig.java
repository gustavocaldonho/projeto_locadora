package br.edu.ifes.col.locadorabackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI locadoraOpenAPI() {
		return new OpenAPI().info(new Info()
				.title("API da Locadora Passatempo")
				.description("Controle de acervo: atores, diretores, classes, títulos e itens")
				.version("Release 1"));
	}

}
