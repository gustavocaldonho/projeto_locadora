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

import br.edu.ifes.col.locadorabackend.domain.Diretor;
import br.edu.ifes.col.locadorabackend.exceptions.ErroPadrao;
import br.edu.ifes.col.locadorabackend.services.DiretorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/resources/diretores")
@Tag(name = "Diretor", description = "Cadastro de diretores")
public class DiretorResource {

	private final DiretorService service;

	public DiretorResource(DiretorService service) {
		this.service = service;
	}

	@GetMapping
	@Operation(summary = "Lista todos os diretores")
	public ResponseEntity<List<Diretor>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}

	@GetMapping("/{id}")
	@Operation(summary = "Busca um diretor pelo id")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Encontrado"),
		@ApiResponse(responseCode = "404", description = "Não encontrado", content = @Content(schema = @Schema(implementation = ErroPadrao.class)))
	})
	public ResponseEntity<Diretor> findById(@PathVariable Long id) {
		return ResponseEntity.ok(service.findById(id));
	}

	@PostMapping
	@Operation(summary = "Inclui um diretor")
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "Criado"),
		@ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = ErroPadrao.class)))
	})
	public ResponseEntity<Diretor> insert(@Valid @RequestBody Diretor diretor) {
		Diretor novo = service.insert(diretor);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(novo.getId()).toUri();
		return ResponseEntity.created(uri).body(novo);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Altera os dados de um diretor")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Alterado"),
		@ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = ErroPadrao.class))),
		@ApiResponse(responseCode = "404", description = "Não encontrado", content = @Content(schema = @Schema(implementation = ErroPadrao.class)))
	})
	public ResponseEntity<Diretor> update(@PathVariable Long id, @Valid @RequestBody Diretor diretor) {
		return ResponseEntity.ok(service.update(id, diretor));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Exclui um diretor")
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
