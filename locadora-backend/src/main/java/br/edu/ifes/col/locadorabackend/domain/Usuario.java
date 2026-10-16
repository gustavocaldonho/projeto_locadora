package br.edu.ifes.col.locadorabackend.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Usuário que faz login no sistema. A senha é guardada criptografada (BCrypt)
 * e nunca é devolvida no JSON.
 */
@Entity
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "O login é obrigatório")
	@Size(max = 50, message = "O login deve ter no máximo 50 caracteres")
	@Column(unique = true)
	private String login;

	/** Só de escrita: o front-end envia, mas a API nunca devolve. */
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String senha;

	@NotBlank(message = "O nome é obrigatório")
	@Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
	private String nome;

	@NotNull(message = "O perfil é obrigatório")
	@Enumerated(EnumType.STRING)
	private Perfil perfil;

	public Usuario() {
	}

	public Usuario(String login, String senha, String nome, Perfil perfil) {
		this.login = login;
		this.senha = senha;
		this.nome = nome;
		this.perfil = perfil;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

}
