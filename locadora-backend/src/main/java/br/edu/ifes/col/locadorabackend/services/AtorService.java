package br.edu.ifes.col.locadorabackend.services;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.edu.ifes.col.locadorabackend.domain.Ator;
import br.edu.ifes.col.locadorabackend.exceptions.IntegridadeException;
import br.edu.ifes.col.locadorabackend.exceptions.ObjetoNaoEncontradoException;
import br.edu.ifes.col.locadorabackend.repositories.AtorRepository;
import br.edu.ifes.col.locadorabackend.repositories.TituloRepository;

@Service
public class AtorService {

	private final AtorRepository repository;
	private final TituloRepository tituloRepository;

	public AtorService(AtorRepository repository, TituloRepository tituloRepository) {
		this.repository = repository;
		this.tituloRepository = tituloRepository;
	}

	public List<Ator> findAll() {
		return repository.findAll(Sort.by("nome"));
	}

	public Ator findById(Long id) {
		return repository.findById(id)
				.orElseThrow(() -> new ObjetoNaoEncontradoException("Ator de id " + id + " não encontrado"));
	}

	public Ator insert(Ator ator) {
		ator.setId(null);
		return repository.save(ator);
	}

	public Ator update(Long id, Ator ator) {
		Ator existente = findById(id);
		existente.setNome(ator.getNome());
		return repository.save(existente);
	}

	public void delete(Long id) {
		findById(id);
		if (tituloRepository.existsByAtoresId(id)) {
			throw new IntegridadeException("Não é possível excluir o ator, pois ele participa de algum título");
		}
		repository.deleteById(id);
	}

}
