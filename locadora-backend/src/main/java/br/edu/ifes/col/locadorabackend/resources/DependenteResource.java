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

import br.edu.ifes.col.locadorabackend.domain.Dependente;
import br.edu.ifes.col.locadorabackend.exceptions.ErroPadrao;
import br.edu.ifes.col.locadorabackend.services.DependenteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/resources/dependentes")
@Tag(name = "Dependente", description = "Cadastro de dependentes dos sócios")
public class DependenteResource {

	private final DependenteService service;

	public DependenteResource(DependenteService service) {
		this.service = service;
	}

	@GetMapping
	@Operation(summary = "Lista todos os dependentes")
	public ResponseEntity<List<Dependente>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}

	@GetMapping("/{id}")
	@Operation(summary = "Busca um dependente pelo número de inscrição")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Encontrado"), @ApiResponse(responseCode = "404", description = "Não encontrado", content = @Content(schema = @Schema(implementation = ErroPadrao.class))) })
	public ResponseEntity<Dependente> findById(@PathVariable Long id) {
		return ResponseEntity.ok(service.findById(id));
	}

	@PostMapping
	@Operation(summary = "Inclui um dependente", description = "Informe o sócio com {\"socio\": {\"numInscricao\": 1}}. O sócio deve estar ativo e ter menos de 3 dependentes ativos")
	@ApiResponses({ @ApiResponse(responseCode = "201", description = "Incluído"), @ApiResponse(responseCode = "400", description = "Dados inválidos ou regra de negócio violada", content = @Content(schema = @Schema(implementation = ErroPadrao.class))), @ApiResponse(responseCode = "404", description = "Não encontrado", content = @Content(schema = @Schema(implementation = ErroPadrao.class))) })
	public ResponseEntity<Dependente> insert(@Valid @RequestBody Dependente dependente) {
		Dependente novo = service.insert(dependente);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(novo.getNumInscricao()).toUri();
		return ResponseEntity.created(uri).body(novo);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Altera os dados de um dependente")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Alterado"), @ApiResponse(responseCode = "400", description = "Dados inválidos ou regra de negócio violada", content = @Content(schema = @Schema(implementation = ErroPadrao.class))), @ApiResponse(responseCode = "404", description = "Não encontrado", content = @Content(schema = @Schema(implementation = ErroPadrao.class))) })
	public ResponseEntity<Dependente> update(@PathVariable Long id, @Valid @RequestBody Dependente dependente) {
		return ResponseEntity.ok(service.update(id, dependente));
	}

	@PutMapping("/{id}/desativar")
	@Operation(summary = "Desativa o dependente")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Desativado"), @ApiResponse(responseCode = "404", description = "Não encontrado", content = @Content(schema = @Schema(implementation = ErroPadrao.class))) })
	public ResponseEntity<Dependente> desativar(@PathVariable Long id) {
		return ResponseEntity.ok(service.desativar(id));
	}

	@PutMapping("/{id}/reativar")
	@Operation(summary = "Reativa o dependente", description = "O sócio deve estar ativo e ter menos de 3 dependentes ativos")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Reativado"), @ApiResponse(responseCode = "400", description = "Dados inválidos ou regra de negócio violada", content = @Content(schema = @Schema(implementation = ErroPadrao.class))), @ApiResponse(responseCode = "404", description = "Não encontrado", content = @Content(schema = @Schema(implementation = ErroPadrao.class))) })
	public ResponseEntity<Dependente> reativar(@PathVariable Long id) {
		return ResponseEntity.ok(service.reativar(id));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Exclui um dependente", description = "Não é permitido se ele tiver locações")
	@ApiResponses({ @ApiResponse(responseCode = "204", description = "Excluído"), @ApiResponse(responseCode = "400", description = "Dados inválidos ou regra de negócio violada", content = @Content(schema = @Schema(implementation = ErroPadrao.class))), @ApiResponse(responseCode = "404", description = "Não encontrado", content = @Content(schema = @Schema(implementation = ErroPadrao.class))) })
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

}
