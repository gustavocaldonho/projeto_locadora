package br.edu.ifes.col.locadorabackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifes.col.locadorabackend.domain.Socio;

public interface SocioRepository extends JpaRepository<Socio, Long> {

	boolean existsByCpf(String cpf);

	boolean existsByCpfAndNumInscricaoNot(String cpf, Long numInscricao);

}
