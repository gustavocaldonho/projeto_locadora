package br.edu.ifes.col.locadorabackend.resources;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifes.col.locadorabackend.dto.DevolucaoInfo;
import br.edu.ifes.col.locadorabackend.exceptions.ErroPadrao;
import br.edu.ifes.col.locadorabackend.services.LocacaoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/resources/devolucoes")
@Tag(name = "Devolução", description = "Efetuar a devolução de um item pelo número de série")
public class DevolucaoResource {

	private final LocacaoService service;

	public DevolucaoResource(LocacaoService service) {
		this.service = service;
	}

	@GetMapping("/{numSerie}")
	@Operation(summary = "Consulta a devolução de um item", description = "Mostra a locação vigente, os dias de atraso, a multa e o valor a pagar, sem registrar nada")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Locação vigente encontrada"), @ApiResponse(responseCode = "400", description = "Dados inválidos ou regra de negócio violada", content = @Content(schema = @Schema(implementation = ErroPadrao.class))), @ApiResponse(responseCode = "404", description = "Não encontrado", content = @Content(schema = @Schema(implementation = ErroPadrao.class))) })
	public ResponseEntity<DevolucaoInfo> consultar(@PathVariable String numSerie) {
		return ResponseEntity.ok(service.consultarDevolucao(numSerie));
	}

	@PostMapping("/{numSerie}")
	@Operation(summary = "Registra a devolução de um item", description = "Grava a data de hoje como devolução efetiva e a multa, se houver")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Devolução registrada"), @ApiResponse(responseCode = "400", description = "Dados inválidos ou regra de negócio violada", content = @Content(schema = @Schema(implementation = ErroPadrao.class))), @ApiResponse(responseCode = "404", description = "Não encontrado", content = @Content(schema = @Schema(implementation = ErroPadrao.class))) })
	public ResponseEntity<DevolucaoInfo> devolver(@PathVariable String numSerie) {
		return ResponseEntity.ok(service.devolver(numSerie));
	}

}
