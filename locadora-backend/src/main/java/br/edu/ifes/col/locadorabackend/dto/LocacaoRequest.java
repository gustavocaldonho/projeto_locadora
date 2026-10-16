package br.edu.ifes.col.locadorabackend.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * Dados enviados pelo front-end para criar ou alterar uma locação.
 * valorCobrado e dtDevolucaoPrevista são opcionais na criação: se vierem vazios,
 * são calculados a partir da classe do título.
 */
public record LocacaoRequest(
		@NotNull(message = "O cliente é obrigatório") Long clienteId,
		@NotNull(message = "O item é obrigatório") Long itemId,
		@PositiveOrZero(message = "O valor não pode ser negativo") Double valorCobrado,
		LocalDate dtDevolucaoPrevista,
		Boolean paga) {
}
