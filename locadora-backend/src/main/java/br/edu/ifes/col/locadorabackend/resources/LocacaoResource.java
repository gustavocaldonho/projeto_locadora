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
import org.springframework.web.bind.annotation.RequestParam;

import br.edu.ifes.col.locadorabackend.domain.Item;
import br.edu.ifes.col.locadorabackend.domain.Locacao;
import br.edu.ifes.col.locadorabackend.dto.LocacaoRequest;
import br.edu.ifes.col.locadorabackend.exceptions.ErroPadrao;
import br.edu.ifes.col.locadorabackend.services.LocacaoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/resources/locacoes")
@Tag(name = "Locação", description = "Efetuar, alterar e cancelar locações")
public class LocacaoResource {

	private final LocacaoService service;

	public LocacaoResource(LocacaoService service) {
		this.service = service;
	}

	@GetMapping
	@Operation(summary = "Lista as locações", description = "Com vigentes=true, lista só as que ainda não foram devolvidas")
	public ResponseEntity<List<Locacao>> findAll(@RequestParam(defaultValue = "false") boolean vigentes) {
		return ResponseEntity.ok(vigentes ? service.findVigentes() : service.findAll());
	}

	@GetMapping("/{id}")
	@Operation(summary = "Busca uma locação pelo id")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Encontrada"), @ApiResponse(responseCode = "404", description = "Não encontrado", content = @Content(schema = @Schema(implementation = ErroPadrao.class))) })
	public ResponseEntity<Locacao> findById(@PathVariable Long id) {
		return ResponseEntity.ok(service.findById(id));
	}

	@GetMapping("/itens-disponiveis")
	@Operation(summary = "Lista os itens que não estão locados no momento")
	public ResponseEntity<List<Item>> findItensDisponiveis() {
		return ResponseEntity.ok(service.findItensDisponiveis());
	}

	@PostMapping
	@Operation(summary = "Efetua uma nova locação", description = "Se valorCobrado ou dtDevolucaoPrevista vierem vazios, são calculados pela classe do título. Bloqueia cliente inativo, cliente em débito e item já locado")
	@ApiResponses({ @ApiResponse(responseCode = "201", description = "Locação criada"), @ApiResponse(responseCode = "400", description = "Dados inválidos ou regra de negócio violada", content = @Content(schema = @Schema(implementation = ErroPadrao.class))), @ApiResponse(responseCode = "404", description = "Não encontrado", content = @Content(schema = @Schema(implementation = ErroPadrao.class))) })
	public ResponseEntity<Locacao> insert(@Valid @RequestBody LocacaoRequest dados) {
		Locacao nova = service.insert(dados);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(nova.getId()).toUri();
		return ResponseEntity.created(uri).body(nova);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Altera o valor, a data prevista ou o pagamento de uma locação vigente")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Alterada"), @ApiResponse(responseCode = "400", description = "Dados inválidos ou regra de negócio violada", content = @Content(schema = @Schema(implementation = ErroPadrao.class))), @ApiResponse(responseCode = "404", description = "Não encontrado", content = @Content(schema = @Schema(implementation = ErroPadrao.class))) })
	public ResponseEntity<Locacao> update(@PathVariable Long id, @RequestBody LocacaoRequest dados) {
		return ResponseEntity.ok(service.update(id, dados));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Cancela uma locação", description = "Só locações não pagas podem ser canceladas")
	@ApiResponses({ @ApiResponse(responseCode = "204", description = "Cancelada"), @ApiResponse(responseCode = "400", description = "Dados inválidos ou regra de negócio violada", content = @Content(schema = @Schema(implementation = ErroPadrao.class))), @ApiResponse(responseCode = "404", description = "Não encontrado", content = @Content(schema = @Schema(implementation = ErroPadrao.class))) })
	public ResponseEntity<Void> cancelar(@PathVariable Long id) {
		service.cancelar(id);
		return ResponseEntity.noContent().build();
	}

}
