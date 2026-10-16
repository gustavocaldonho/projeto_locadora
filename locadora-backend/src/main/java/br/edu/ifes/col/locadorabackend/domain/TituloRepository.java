package br.edu.ifes.col.locadorabackend.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TituloRepository extends JpaRepository<Titulo, Long> {

	boolean existsByAtoresId(Long atorId);

	boolean existsByDiretorId(Long diretorId);

	boolean existsByClasseId(Long classeId);

}
