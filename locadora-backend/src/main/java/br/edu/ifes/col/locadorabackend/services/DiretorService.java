package br.edu.ifes.col.locadorabackend.services;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.edu.ifes.col.locadorabackend.domain.Diretor;
import br.edu.ifes.col.locadorabackend.domain.DiretorRepository;
import br.edu.ifes.col.locadorabackend.domain.TituloRepository;
import br.edu.ifes.col.locadorabackend.exceptions.IntegridadeException;
import br.edu.ifes.col.locadorabackend.exceptions.ObjetoNaoEncontradoException;

@Service
public class DiretorService {

	private final DiretorRepository repository;
	private final TituloRepository tituloRepository;

	public DiretorService(DiretorRepository repository, TituloRepository tituloRepository) {
		this.repository = repository;
		this.tituloRepository = tituloRepository;
	}

	public List<Diretor> findAll() {
		return repository.findAll(Sort.by("nome"));
	}

	public Diretor findById(Long id) {
		return repository.findById(id)
				.orElseThrow(() -> new ObjetoNaoEncontradoException("Diretor de id " + id + " não encontrado"));
	}

	public Diretor insert(Diretor diretor) {
		diretor.setId(null);
		return repository.save(diretor);
	}

	public Diretor update(Long id, Diretor diretor) {
		Diretor existente = findById(id);
		existente.setNome(diretor.getNome());
		return repository.save(existente);
	}

	public void delete(Long id) {
		findById(id);
		if (tituloRepository.existsByDiretorId(id)) {
			throw new IntegridadeException("Não é possível excluir o diretor, pois ele dirigiu algum título");
		}
		repository.deleteById(id);
	}

}
