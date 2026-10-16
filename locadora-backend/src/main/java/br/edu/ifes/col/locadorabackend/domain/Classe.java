package br.edu.ifes.col.locadorabackend.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

/**
 * A classe define o valor da locação e o prazo de devolução (em dias)
 * dos títulos que pertencem a ela.
 */
@Entity
public class Classe {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "O nome é obrigatório")
	@Size(max = 50, message = "O nome deve ter no máximo 50 caracteres")
	private String nome;

	@NotNull(message = "O valor é obrigatório")
	@PositiveOrZero(message = "O valor não pode ser negativo")
	private Double valor;

	@NotNull(message = "O prazo de devolução é obrigatório")
	@Positive(message = "O prazo de devolução deve ser de pelo menos 1 dia")
	private Integer prazoDevolucao;

	public Classe() {
	}

	public Classe(Long id, String nome, Double valor, Integer prazoDevolucao) {
		this.id = id;
		this.nome = nome;
		this.valor = valor;
		this.prazoDevolucao = prazoDevolucao;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public Integer getPrazoDevolucao() {
		return prazoDevolucao;
	}

	public void setPrazoDevolucao(Integer prazoDevolucao) {
		this.prazoDevolucao = prazoDevolucao;
	}

}
