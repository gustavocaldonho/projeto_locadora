package br.edu.ifes.col.locadorabackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifes.col.locadorabackend.domain.Titulo;

public interface TituloRepository extends JpaRepository<Titulo, Long> {

	boolean existsByAtoresId(Long atorId);

	boolean existsByDiretorId(Long diretorId);

	boolean existsByClasseId(Long classeId);

}
