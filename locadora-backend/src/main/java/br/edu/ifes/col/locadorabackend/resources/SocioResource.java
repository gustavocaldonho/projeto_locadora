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
import br.edu.ifes.col.locadorabackend.domain.Socio;
import br.edu.ifes.col.locadorabackend.exceptions.ErroPadrao;
import br.edu.ifes.col.locadorabackend.services.SocioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/resources/socios")
@Tag(name = "Sócio", description = "Cadastro de sócios (clientes titulares)")
public class SocioResource {

	private final SocioService service;

	public SocioResource(SocioService service) {
		this.service = service;
	}

	@GetMapping
	@Operation(summary = "Lista todos os sócios")
	public ResponseEntity<List<Socio>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}

	@GetMapping("/{id}")
	@Operation(summary = "Busca um sócio pelo número de inscrição")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Encontrado"), @ApiResponse(responseCode = "404", description = "Não encontrado", content = @Content(schema = @Schema(implementation = ErroPadrao.class))) })
	public ResponseEntity<Socio> findById(@PathVariable Long id) {
		return ResponseEntity.ok(service.findById(id));
	}

	@GetMapping("/{id}/dependentes")
	@Operation(summary = "Lista os dependentes de um sócio")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Encontrados"), @ApiResponse(responseCode = "404", description = "Não encontrado", content = @Content(schema = @Schema(implementation = ErroPadrao.class))) })
	public ResponseEntity<List<Dependente>> findDependentes(@PathVariable Long id) {
		return ResponseEntity.ok(service.findDependentes(id));
	}

	@PostMapping
	@Operation(summary = "Inscreve um novo sócio", description = "O número de inscrição é gerado pelo sistema e o sócio começa ativo")
	@ApiResponses({ @ApiResponse(responseCode = "201", description = "Inscrito"), @ApiResponse(responseCode = "400", description = "Dados inválidos ou regra de negócio violada", content = @Content(schema = @Schema(implementation = ErroPadrao.class))) })
	public ResponseEntity<Socio> insert(@Valid @RequestBody Socio socio) {
		Socio novo = service.insert(socio);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(novo.getNumInscricao()).toUri();
		return ResponseEntity.created(uri).body(novo);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Altera os dados de um sócio")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Alterado"), @ApiResponse(responseCode = "400", description = "Dados inválidos ou regra de negócio violada", content = @Content(schema = @Schema(implementation = ErroPadrao.class))), @ApiResponse(responseCode = "404", description = "Não encontrado", content = @Content(schema = @Schema(implementation = ErroPadrao.class))) })
	public ResponseEntity<Socio> update(@PathVariable Long id, @Valid @RequestBody Socio socio) {
		return ResponseEntity.ok(service.update(id, socio));
	}

	@PutMapping("/{id}/desativar")
	@Operation(summary = "Desativa o sócio e todos os seus dependentes")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Desativado"), @ApiResponse(responseCode = "404", description = "Não encontrado", content = @Content(schema = @Schema(implementation = ErroPadrao.class))) })
	public ResponseEntity<Socio> desativar(@PathVariable Long id) {
		return ResponseEntity.ok(service.desativar(id));
	}

	@PutMapping("/{id}/reativar")
	@Operation(summary = "Reativa o sócio e até três de seus dependentes")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Reativado"), @ApiResponse(responseCode = "404", description = "Não encontrado", content = @Content(schema = @Schema(implementation = ErroPadrao.class))) })
	public ResponseEntity<Socio> reativar(@PathVariable Long id) {
		return ResponseEntity.ok(service.reativar(id));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Exclui o sócio e seus dependentes", description = "Não é permitido se o sócio ou algum dependente tiver locações")
	@ApiResponses({ @ApiResponse(responseCode = "204", description = "Excluído"), @ApiResponse(responseCode = "400", description = "Dados inválidos ou regra de negócio violada", content = @Content(schema = @Schema(implementation = ErroPadrao.class))), @ApiResponse(responseCode = "404", description = "Não encontrado", content = @Content(schema = @Schema(implementation = ErroPadrao.class))) })
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

}
