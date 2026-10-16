package br.edu.ifes.col.locadorabackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifes.col.locadorabackend.domain.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {

	boolean existsByTituloId(Long tituloId);

	boolean existsByNumSerie(String numSerie);

	boolean existsByNumSerieAndIdNot(String numSerie, Long id);

}
