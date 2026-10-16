package br.edu.ifes.col.locadorabackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI locadoraOpenAPI() {
		// O botão "Authorize" do Swagger pede login e senha e envia em todas as requisições
		return new OpenAPI()
				.info(new Info()
						.title("API da Locadora Passatempo")
						.description("Controle de acervo, atendimento a cliente e consulta de títulos. "
								+ "Use o botão Authorize com um usuário (ex.: admin / admin123).")
						.version("Release 3"))
				.components(new Components().addSecuritySchemes("basic",
						new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("basic")))
				.addSecurityItem(new SecurityRequirement().addList("basic"));
	}

}
