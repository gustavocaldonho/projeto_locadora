package br.edu.ifes.col.locadorabackend.dto;

import br.edu.ifes.col.locadorabackend.domain.Locacao;

/**
 * Resumo de uma devolução: a locação, os dias de atraso e quanto o cliente deve pagar.
 */
public record DevolucaoInfo(
		Locacao locacao,
		long diasAtraso,
		double valorLocacao,
		double multa,
		double valorAPagar) {
}
