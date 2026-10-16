package br.edu.ifes.col.locadorabackend.services;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.edu.ifes.col.locadorabackend.domain.Classe;
import br.edu.ifes.col.locadorabackend.exceptions.IntegridadeException;
import br.edu.ifes.col.locadorabackend.exceptions.ObjetoNaoEncontradoException;
import br.edu.ifes.col.locadorabackend.repositories.ClasseRepository;
import br.edu.ifes.col.locadorabackend.repositories.TituloRepository;

@Service
public class ClasseService {

	private final ClasseRepository repository;
	private final TituloRepository tituloRepository;

	public ClasseService(ClasseRepository repository, TituloRepository tituloRepository) {
		this.repository = repository;
		this.tituloRepository = tituloRepository;
	}

	public List<Classe> findAll() {
		return repository.findAll(Sort.by("nome"));
	}

	public Classe findById(Long id) {
		return repository.findById(id)
				.orElseThrow(() -> new ObjetoNaoEncontradoException("Classe de id " + id + " não encontrada"));
	}

	public Classe insert(Classe classe) {
		classe.setId(null);
		return repository.save(classe);
	}

	public Classe update(Long id, Classe classe) {
		Classe existente = findById(id);
		existente.setNome(classe.getNome());
		existente.setValor(classe.getValor());
		existente.setPrazoDevolucao(classe.getPrazoDevolucao());
		return repository.save(existente);
	}

	public void delete(Long id) {
		findById(id);
		if (tituloRepository.existsByClasseId(id)) {
			throw new IntegridadeException("Não é possível excluir a classe, pois existem títulos ligados a ela");
		}
		repository.deleteById(id);
	}

}
