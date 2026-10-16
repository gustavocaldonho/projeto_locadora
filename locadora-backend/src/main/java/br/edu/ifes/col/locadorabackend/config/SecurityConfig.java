package br.edu.ifes.col.locadorabackend.config;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.edu.ifes.col.locadorabackend.exceptions.ErroPadrao;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Login e controle de acesso com Spring Security.
 *
 * O front-end envia o login e a senha em toda requisição, no cabeçalho
 * "Authorization: Basic ..." (HTTP Basic). Não há sessão no servidor.
 *
 * - Swagger: liberado.
 * - Consulta de títulos: funcionário e cliente.
 * - Todo o resto da API: só funcionário.
 */
@Configuration
public class SecurityConfig {

	private final ObjectMapper objectMapper;

	public SecurityConfig(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.csrf(csrf -> csrf.disable())
				.cors(Customizer.withDefaults())
				.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth
						.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
						.requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
						.requestMatchers("/resources/auth/**").authenticated()
						.requestMatchers(HttpMethod.GET, "/resources/titulos/consulta/**").hasAnyRole("FUNCIONARIO", "CLIENTE")
						.requestMatchers("/resources/**").hasRole("FUNCIONARIO")
						.anyRequest().permitAll())
				.httpBasic(basic -> basic.authenticationEntryPoint(
						(request, response, e) -> escreverErro(request, response, HttpStatus.UNAUTHORIZED,
								"Não autenticado", "Faça login com um usuário e senha válidos")))
				.exceptionHandling(ex -> ex
						.authenticationEntryPoint((request, response, e) -> escreverErro(request, response,
								HttpStatus.UNAUTHORIZED, "Não autenticado", "Faça login com um usuário e senha válidos"))
						.accessDeniedHandler((request, response, e) -> escreverErro(request, response,
								HttpStatus.FORBIDDEN, "Acesso negado", "Seu perfil não tem permissão para esta operação")));
		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * Responde 401/403 no mesmo formato ErroPadrao do resto da API.
	 * Também evita o cabeçalho WWW-Authenticate, que faria o navegador abrir
	 * a janelinha de login dele.
	 */
	private void escreverErro(HttpServletRequest request, HttpServletResponse response, HttpStatus status,
			String error, String message) throws IOException {
		ErroPadrao erro = new ErroPadrao(LocalDateTime.now(), status.value(), error, message, request.getRequestURI());
		response.setStatus(status.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("UTF-8");
		objectMapper.writeValue(response.getOutputStream(), erro);
	}

}
