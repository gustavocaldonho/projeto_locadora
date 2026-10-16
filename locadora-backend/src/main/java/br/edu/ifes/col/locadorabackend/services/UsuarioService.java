package br.edu.ifes.col.locadorabackend.services;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.edu.ifes.col.locadorabackend.domain.Perfil;
import br.edu.ifes.col.locadorabackend.domain.Usuario;
import br.edu.ifes.col.locadorabackend.exceptions.IntegridadeException;
import br.edu.ifes.col.locadorabackend.exceptions.ObjetoNaoEncontradoException;
import br.edu.ifes.col.locadorabackend.exceptions.RegraNegocioException;
import br.edu.ifes.col.locadorabackend.repositories.UsuarioRepository;

/**
 * Cadastro de usuários. Também é o UserDetailsService: o Spring Security chama
 * loadUserByUsername no login para buscar o usuário e conferir a senha.
 */
@Service
public class UsuarioService implements UserDetailsService {

	private final UsuarioRepository repository;
	private final PasswordEncoder passwordEncoder;

	public UsuarioService(UsuarioRepository repository, PasswordEncoder passwordEncoder) {
		this.repository = repository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		Usuario usuario = repository.findByLogin(login)
				.orElseThrow(() -> new UsernameNotFoundException("Usuário " + login + " não encontrado"));
		// O perfil vira o papel (role) usado nas regras de acesso do SecurityConfig
		return User.withUsername(usuario.getLogin())
				.password(usuario.getSenha())
				.roles(usuario.getPerfil().name())
				.build();
	}

	public Usuario findByLogin(String login) {
		return repository.findByLogin(login)
				.orElseThrow(() -> new ObjetoNaoEncontradoException("Usuário " + login + " não encontrado"));
	}

	public List<Usuario> findAll() {
		return repository.findAll(Sort.by("nome"));
	}

	public Usuario findById(Long id) {
		return repository.findById(id)
				.orElseThrow(() -> new ObjetoNaoEncontradoException("Usuário de id " + id + " não encontrado"));
	}

	public Usuario insert(Usuario usuario) {
		if (usuario.getSenha() == null || usuario.getSenha().isBlank()) {
			throw new RegraNegocioException("A senha é obrigatória");
		}
		if (repository.existsByLogin(usuario.getLogin())) {
			throw new IntegridadeException("Já existe um usuário com o login " + usuario.getLogin());
		}
		usuario.setId(null);
		usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
		return repository.save(usuario);
	}

	/** Se a senha vier em branco, mantém a senha atual. */
	public Usuario update(Long id, Usuario usuario) {
		Usuario existente = findById(id);
		if (repository.existsByLoginAndIdNot(usuario.getLogin(), id)) {
			throw new IntegridadeException("Já existe um usuário com o login " + usuario.getLogin());
		}
		if (existente.getPerfil() == Perfil.FUNCIONARIO && usuario.getPerfil() != Perfil.FUNCIONARIO) {
			verificarOutroFuncionario();
		}
		existente.setLogin(usuario.getLogin());
		existente.setNome(usuario.getNome());
		existente.setPerfil(usuario.getPerfil());
		if (usuario.getSenha() != null && !usuario.getSenha().isBlank()) {
			existente.setSenha(passwordEncoder.encode(usuario.getSenha()));
		}
		return repository.save(existente);
	}

	public void delete(Long id) {
		Usuario usuario = findById(id);
		if (usuario.getPerfil() == Perfil.FUNCIONARIO) {
			verificarOutroFuncionario();
		}
		repository.deleteById(id);
	}

	/** Impede ficar sem nenhum funcionário, senão ninguém mais consegue administrar o sistema. */
	private void verificarOutroFuncionario() {
		if (repository.countByPerfil(Perfil.FUNCIONARIO) <= 1) {
			throw new RegraNegocioException("O sistema precisa ter pelo menos um usuário funcionário");
		}
	}

}
