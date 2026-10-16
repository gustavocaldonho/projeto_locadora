package br.edu.ifes.col.locadorabackend.dto;

import java.time.LocalDate;
import java.util.List;

/**
 * Dados de um título mostrados na consulta (caso de uso Consultar Título),
 * incluindo quantas cópias estão disponíveis para locação.
 * previsaoDisponibilidade só é preenchida quando não há cópia disponível:
 * é a data prevista de devolução mais próxima.
 */
public record TituloConsulta(
		Long id,
		String nome,
		Integer ano,
		String categoria,
		String sinopse,
		String diretor,
		List<String> atores,
		String classe,
		Double valorLocacao,
		Integer prazoDevolucao,
		long totalItens,
		long itensDisponiveis,
		LocalDate previsaoDisponibilidade) {
}
