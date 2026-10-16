package br.edu.ifes.col.locadorabackend.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Um título (filme) tem um ou mais atores, um diretor e pertence a uma classe.
 */
@Entity
public class Titulo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "O nome é obrigatório")
	@Size(max = 150, message = "O nome deve ter no máximo 150 caracteres")
	private String nome;

	@NotNull(message = "O ano é obrigatório")
	@Min(value = 1888, message = "Ano inválido")
	@Max(value = 2100, message = "Ano inválido")
	private Integer ano;

	@Size(max = 2000, message = "A sinopse deve ter no máximo 2000 caracteres")
	@Column(length = 2000)
	private String sinopse;

	@NotBlank(message = "A categoria é obrigatória")
	@Size(max = 50, message = "A categoria deve ter no máximo 50 caracteres")
	private String categoria;

	@NotEmpty(message = "Informe pelo menos um ator")
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "titulo_ator",
			joinColumns = @JoinColumn(name = "titulo_id"),
			inverseJoinColumns = @JoinColumn(name = "ator_id"))
	private List<Ator> atores = new ArrayList<>();

	@NotNull(message = "O diretor é obrigatório")
	@ManyToOne
	@JoinColumn(name = "diretor_id")
	private Diretor diretor;

	@NotNull(message = "A classe é obrigatória")
	@ManyToOne
	@JoinColumn(name = "classe_id")
	private Classe classe;

	public Titulo() {
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

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public String getSinopse() {
		return sinopse;
	}

	public void setSinopse(String sinopse) {
		this.sinopse = sinopse;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public List<Ator> getAtores() {
		return atores;
	}

	public void setAtores(List<Ator> atores) {
		this.atores = atores;
	}

	public Diretor getDiretor() {
		return diretor;
	}

	public void setDiretor(Diretor diretor) {
		this.diretor = diretor;
	}

	public Classe getClasse() {
		return classe;
	}

	public void setClasse(Classe classe) {
		this.classe = classe;
	}

}
