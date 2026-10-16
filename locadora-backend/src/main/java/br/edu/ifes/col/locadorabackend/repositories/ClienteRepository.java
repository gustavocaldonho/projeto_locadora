package br.edu.ifes.col.locadorabackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifes.col.locadorabackend.domain.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

}
