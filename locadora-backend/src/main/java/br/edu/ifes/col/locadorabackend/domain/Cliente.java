package br.edu.ifes.col.locadorabackend.domain;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

/**
 * Cliente da locadora. É abstrata: todo cliente é um Sócio ou um Dependente.
 * Na herança JOINED, a tabela "cliente" guarda os campos comuns e as tabelas
 * "socio" e "dependente" guardam só os campos próprios de cada um.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Cliente {

	/** Número de inscrição: gerado pelo sistema e nunca alterado. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long numInscricao;

	@NotBlank(message = "O nome é obrigatório")
	@Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
	private String nome;

	@NotNull(message = "A data de nascimento é obrigatória")
	@Past(message = "A data de nascimento deve estar no passado")
	private LocalDate dtNascimento;

	@NotNull(message = "O sexo é obrigatório")
	@Enumerated(EnumType.STRING)
	private Sexo sexo;

	private Boolean estahAtivo = true;

	/** "SOCIO" ou "DEPENDENTE", para o front-end saber o tipo do cliente. */
	public abstract String getTipo();

	public Long getNumInscricao() {
		return numInscricao;
	}

	public void setNumInscricao(Long numInscricao) {
		this.numInscricao = numInscricao;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public LocalDate getDtNascimento() {
		return dtNascimento;
	}

	public void setDtNascimento(LocalDate dtNascimento) {
		this.dtNascimento = dtNascimento;
	}

	public Sexo getSexo() {
		return sexo;
	}

	public void setSexo(Sexo sexo) {
		this.sexo = sexo;
	}

	public Boolean getEstahAtivo() {
		return estahAtivo;
	}

	public void setEstahAtivo(Boolean estahAtivo) {
		this.estahAtivo = estahAtivo;
	}

}
