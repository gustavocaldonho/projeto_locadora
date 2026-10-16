package br.edu.ifes.col.locadorabackend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifes.col.locadorabackend.domain.Perfil;
import br.edu.ifes.col.locadorabackend.domain.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	Optional<Usuario> findByLogin(String login);

	boolean existsByLogin(String login);

	boolean existsByLoginAndIdNot(String login, Long id);

	long countByPerfil(Perfil perfil);

}
