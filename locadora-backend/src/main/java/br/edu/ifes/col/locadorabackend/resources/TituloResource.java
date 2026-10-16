package br.edu.ifes.col.locadorabackend.resources;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.edu.ifes.col.locadorabackend.domain.Titulo;
import br.edu.ifes.col.locadorabackend.exceptions.ErroPadrao;
import br.edu.ifes.col.locadorabackend.services.TituloService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/resources/titulos")
@Tag(name = "Titulo", description = "Cadastro de títulos")
public class TituloResource {

	private final TituloService service;

	public TituloResource(TituloService service) {
		this.service = service;
	}

	@GetMapping
	@Operation(summary = "Lista todos os títulos")
	public ResponseEntity<List<Titulo>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}

	@GetMapping("/{id}")
	@Operation(summary = "Busca um título pelo id")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Encontrado"),
		@ApiResponse(responseCode = "404", description = "Não encontrado", content = @Content(schema = @Schema(implementation = ErroPadrao.class)))
	})
	public ResponseEntity<Titulo> findById(@PathVariable Long id) {
		return ResponseEntity.ok(service.findById(id));
	}

	@PostMapping
	@Operation(summary = "Inclui um título")
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "Criado"),
		@ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = ErroPadrao.class)))
	})
	public ResponseEntity<Titulo> insert(@Valid @RequestBody Titulo titulo) {
		Titulo novo = service.insert(titulo);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(novo.getId()).toUri();
		return ResponseEntity.created(uri).body(novo);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Altera os dados de um título")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Alterado"),
		@ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = ErroPadrao.class))),
		@ApiResponse(responseCode = "404", description = "Não encontrado", content = @Content(schema = @Schema(implementation = ErroPadrao.class)))
	})
	public ResponseEntity<Titulo> update(@PathVariable Long id, @Valid @RequestBody Titulo titulo) {
		return ResponseEntity.ok(service.update(id, titulo));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Exclui um título")
	@ApiResponses({
		@ApiResponse(responseCode = "204", description = "Excluído"),
		@ApiResponse(responseCode = "400", description = "Não pode ser excluído (está em uso)", content = @Content(schema = @Schema(implementation = ErroPadrao.class))),
		@ApiResponse(responseCode = "404", description = "Não encontrado", content = @Content(schema = @Schema(implementation = ErroPadrao.class)))
	})
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

}
