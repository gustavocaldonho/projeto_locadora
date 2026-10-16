package br.edu.ifes.col.locadorabackend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifes.col.locadorabackend.domain.Dependente;

public interface DependenteRepository extends JpaRepository<Dependente, Long> {

	List<Dependente> findBySocioNumInscricaoOrderByNumInscricao(Long socioId);

	long countBySocioNumInscricaoAndEstahAtivoTrue(Long socioId);

}
