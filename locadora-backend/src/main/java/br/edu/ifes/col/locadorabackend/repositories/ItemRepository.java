package br.edu.ifes.col.locadorabackend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifes.col.locadorabackend.domain.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {

	Optional<Item> findByNumSerie(String numSerie);

	boolean existsByTituloId(Long tituloId);

	long countByTituloId(Long tituloId);

	boolean existsByNumSerie(String numSerie);

	boolean existsByNumSerieAndIdNot(String numSerie, Long id);

}
