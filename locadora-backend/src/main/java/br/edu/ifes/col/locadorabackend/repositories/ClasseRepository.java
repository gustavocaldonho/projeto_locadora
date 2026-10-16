package br.edu.ifes.col.locadorabackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifes.col.locadorabackend.domain.Classe;

public interface ClasseRepository extends JpaRepository<Classe, Long> {

}
