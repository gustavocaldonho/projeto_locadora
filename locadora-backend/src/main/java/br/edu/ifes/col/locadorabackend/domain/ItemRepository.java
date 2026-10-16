package br.edu.ifes.col.locadorabackend.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {

	boolean existsByTituloId(Long tituloId);

	boolean existsByNumSerie(String numSerie);

	boolean existsByNumSerieAndIdNot(String numSerie, Long id);

}
