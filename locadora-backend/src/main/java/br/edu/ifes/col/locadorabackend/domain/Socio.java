package br.edu.ifes.col.locadorabackend.domain;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Sócio: o titular da inscrição. Pode ter dependentes (no máximo 3 ativos).
 */
@Entity
public class Socio extends Cliente {

	@NotBlank(message = "O CPF é obrigatório")
	@Pattern(regexp = "\\d{3}\\.?\\d{3}\\.?\\d{3}-?\\d{2}", message = "CPF inválido (use 000.000.000-00)")
	@Column(unique = true)
	private String cpf;

	@NotBlank(message = "O endereço é obrigatório")
	@Size(max = 200, message = "O endereço deve ter no máximo 200 caracteres")
	private String endereco;

	@NotBlank(message = "O telefone é obrigatório")
	@Size(max = 20, message = "O telefone deve ter no máximo 20 caracteres")
	private String tel;

	// Não vai no JSON do sócio; os dependentes têm o próprio endpoint
	@JsonIgnore
	@OneToMany(mappedBy = "socio")
	private List<Dependente> dependentes = new ArrayList<>();

	@Override
	public String getTipo() {
		return "SOCIO";
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public List<Dependente> getDependentes() {
		return dependentes;
	}

	public void setDependentes(List<Dependente> dependentes) {
		this.dependentes = dependentes;
	}

}
