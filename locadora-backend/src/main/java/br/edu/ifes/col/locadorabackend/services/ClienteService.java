package br.edu.ifes.col.locadorabackend.services;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.edu.ifes.col.locadorabackend.domain.Cliente;
import br.edu.ifes.col.locadorabackend.exceptions.ObjetoNaoEncontradoException;
import br.edu.ifes.col.locadorabackend.repositories.ClienteRepository;

/**
 * Consulta de clientes em geral (sócios e dependentes juntos), usada na locação.
 */
@Service
public class ClienteService {

	private final ClienteRepository repository;

	public ClienteService(ClienteRepository repository) {
		this.repository = repository;
	}

	public List<Cliente> findAll() {
		return repository.findAll(Sort.by("nome"));
	}

	public Cliente findById(Long id) {
		return repository.findById(id)
				.orElseThrow(() -> new ObjetoNaoEncontradoException("Cliente de inscrição " + id + " não encontrado"));
	}

}
