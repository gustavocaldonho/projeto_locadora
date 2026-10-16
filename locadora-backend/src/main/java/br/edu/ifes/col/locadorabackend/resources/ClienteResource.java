package br.edu.ifes.col.locadorabackend.resources;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifes.col.locadorabackend.domain.Cliente;
import br.edu.ifes.col.locadorabackend.exceptions.ErroPadrao;
import br.edu.ifes.col.locadorabackend.services.ClienteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/resources/clientes")
@Tag(name = "Cliente", description = "Consulta de clientes em geral (sócios e dependentes)")
public class ClienteResource {

	private final ClienteService service;

	public ClienteResource(ClienteService service) {
		this.service = service;
	}

	@GetMapping
	@Operation(summary = "Lista todos os clientes, sócios e dependentes")
	public ResponseEntity<List<Cliente>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}

	@GetMapping("/{id}")
	@Operation(summary = "Busca um cliente pelo número de inscrição")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Encontrado"), @ApiResponse(responseCode = "404", description = "Não encontrado", content = @Content(schema = @Schema(implementation = ErroPadrao.class))) })
	public ResponseEntity<Cliente> findById(@PathVariable Long id) {
		return ResponseEntity.ok(service.findById(id));
	}

}
