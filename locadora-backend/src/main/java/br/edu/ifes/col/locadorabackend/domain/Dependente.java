package br.edu.ifes.col.locadorabackend.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

/**
 * Dependente: cliente vinculado a um sócio.
 */
@Entity
public class Dependente extends Cliente {

	@NotNull(message = "O sócio é obrigatório")
	@ManyToOne
	@JoinColumn(name = "socio_id")
	private Socio socio;

	@Override
	public String getTipo() {
		return "DEPENDENTE";
	}

	public Socio getSocio() {
		return socio;
	}

	public void setSocio(Socio socio) {
		this.socio = socio;
	}

}
