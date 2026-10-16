package br.edu.ifes.col.locadorabackend.domain;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

/**
 * Um item é a cópia física de um título (fita, DVD ou Blu-ray).
 */
@Entity
public class Item {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "O número de série é obrigatório")
	@Size(max = 50, message = "O número de série deve ter no máximo 50 caracteres")
	@Column(unique = true)
	private String numSerie;

	@NotNull(message = "A data de aquisição é obrigatória")
	@PastOrPresent(message = "A data de aquisição não pode ser futura")
	private LocalDate dtAquisicao;

	@NotNull(message = "O tipo do item é obrigatório")
	@Enumerated(EnumType.STRING)
	private TipoItem tipoItem;

	@NotNull(message = "O título é obrigatório")
	@ManyToOne
	@JoinColumn(name = "titulo_id")
	private Titulo titulo;

	public Item() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNumSerie() {
		return numSerie;
	}

	public void setNumSerie(String numSerie) {
		this.numSerie = numSerie;
	}

	public LocalDate getDtAquisicao() {
		return dtAquisicao;
	}

	public void setDtAquisicao(LocalDate dtAquisicao) {
		this.dtAquisicao = dtAquisicao;
	}

	public TipoItem getTipoItem() {
		return tipoItem;
	}

	public void setTipoItem(TipoItem tipoItem) {
		this.tipoItem = tipoItem;
	}

	public Titulo getTitulo() {
		return titulo;
	}

	public void setTitulo(Titulo titulo) {
		this.titulo = titulo;
	}

}
