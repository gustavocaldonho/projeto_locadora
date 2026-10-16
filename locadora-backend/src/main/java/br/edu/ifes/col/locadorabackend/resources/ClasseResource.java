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

import br.edu.ifes.col.locadorabackend.domain.Classe;
import br.edu.ifes.col.locadorabackend.exceptions.ErroPadrao;
import br.edu.ifes.col.locadorabackend.services.ClasseService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/resources/classes")
@Tag(name = "Classe", description = "Cadastro de classes (valor e prazo de devolução)")
public class ClasseResource {

	private final ClasseService service;

	public ClasseResource(ClasseService service) {
		this.service = service;
	}

	@GetMapping
	@Operation(summary = "Lista todas as classes")
	public ResponseEntity<List<Classe>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}

	@GetMapping("/{id}")
	@Operation(summary = "Busca uma classe pelo id")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Encontrada"),
		@ApiResponse(responseCode = "404", description = "Não encontrada", content = @Content(schema = @Schema(implementation = ErroPadrao.class)))
	})
	public ResponseEntity<Classe> findById(@PathVariable Long id) {
		return ResponseEntity.ok(service.findById(id));
	}

	@PostMapping
	@Operation(summary = "Inclui uma classe")
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "Criada"),
		@ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = ErroPadrao.class)))
	})
	public ResponseEntity<Classe> insert(@Valid @RequestBody Classe classe) {
		Classe novo = service.insert(classe);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(novo.getId()).toUri();
		return ResponseEntity.created(uri).body(novo);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Altera os dados de uma classe")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Alterada"),
		@ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = ErroPadrao.class))),
		@ApiResponse(responseCode = "404", description = "Não encontrada", content = @Content(schema = @Schema(implementation = ErroPadrao.class)))
	})
	public ResponseEntity<Classe> update(@PathVariable Long id, @Valid @RequestBody Classe classe) {
		return ResponseEntity.ok(service.update(id, classe));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Exclui uma classe")
	@ApiResponses({
		@ApiResponse(responseCode = "204", description = "Excluída"),
		@ApiResponse(responseCode = "400", description = "Não pode ser excluída (está em uso)", content = @Content(schema = @Schema(implementation = ErroPadrao.class))),
		@ApiResponse(responseCode = "404", description = "Não encontrada", content = @Content(schema = @Schema(implementation = ErroPadrao.class)))
	})
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

}
