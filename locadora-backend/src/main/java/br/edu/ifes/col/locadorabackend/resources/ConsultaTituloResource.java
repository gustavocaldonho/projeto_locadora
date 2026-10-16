package br.edu.ifes.col.locadorabackend.resources;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifes.col.locadorabackend.domain.Ator;
import br.edu.ifes.col.locadorabackend.dto.TituloConsulta;
import br.edu.ifes.col.locadorabackend.services.ConsultaTituloService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Consulta de títulos, liberada para funcionários e clientes (veja SecurityConfig).
 */
@RestController
@RequestMapping("/resources/titulos/consulta")
@Tag(name = "Consulta de Títulos", description = "Consultar títulos por nome, categoria e ator (funcionário e cliente)")
public class ConsultaTituloResource {

	private final ConsultaTituloService service;

	public ConsultaTituloResource(ConsultaTituloService service) {
		this.service = service;
	}

	@GetMapping
	@Operation(summary = "Consulta títulos", description = "Filtros opcionais e combináveis. Mostra também as cópias disponíveis")
	public ResponseEntity<List<TituloConsulta>> consultar(
			@Parameter(description = "Parte do nome do título") @RequestParam(required = false) String nome,
			@Parameter(description = "Categoria exata, ex.: Drama") @RequestParam(required = false) String categoria,
			@Parameter(description = "Id do ator") @RequestParam(required = false) Long atorId) {
		return ResponseEntity.ok(service.consultar(nome, categoria, atorId));
	}

	@GetMapping("/categorias")
	@Operation(summary = "Lista as categorias existentes")
	public ResponseEntity<List<String>> categorias() {
		return ResponseEntity.ok(service.categorias());
	}

	@GetMapping("/atores")
	@Operation(summary = "Lista os atores, para a consulta por ator")
	public ResponseEntity<List<Ator>> atores() {
		return ResponseEntity.ok(service.atores());
	}

}
