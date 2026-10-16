package br.edu.ifes.col.locadorabackend.resources;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifes.col.locadorabackend.domain.Usuario;
import br.edu.ifes.col.locadorabackend.exceptions.ErroPadrao;
import br.edu.ifes.col.locadorabackend.services.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/resources/auth")
@Tag(name = "Autenticação", description = "Login com HTTP Basic")
public class AuthResource {

	private final UsuarioService service;

	public AuthResource(UsuarioService service) {
		this.service = service;
	}

	/**
	 * O login do front-end chama este endpoint com o cabeçalho Authorization.
	 * Se o usuário e a senha estiverem certos, devolve os dados do usuário (sem a senha).
	 */
	@GetMapping("/usuario")
	@Operation(summary = "Confere o login e devolve o usuário autenticado")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Login válido"),
		@ApiResponse(responseCode = "401", description = "Usuário ou senha inválidos", content = @Content(schema = @Schema(implementation = ErroPadrao.class)))
	})
	public ResponseEntity<Usuario> usuarioLogado(Principal principal) {
		return ResponseEntity.ok(service.findByLogin(principal.getName()));
	}

}
