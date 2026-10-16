package br.edu.ifes.col.locadorabackend.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.edu.ifes.col.locadorabackend.domain.Ator;
import br.edu.ifes.col.locadorabackend.domain.ItemRepository;
import br.edu.ifes.col.locadorabackend.domain.Titulo;
import br.edu.ifes.col.locadorabackend.domain.TituloRepository;
import br.edu.ifes.col.locadorabackend.exceptions.IntegridadeException;
import br.edu.ifes.col.locadorabackend.exceptions.ObjetoNaoEncontradoException;

@Service
public class TituloService {

	private final TituloRepository repository;
	private final ItemRepository itemRepository;
	private final AtorService atorService;
	private final DiretorService diretorService;
	private final ClasseService classeService;

	public TituloService(TituloRepository repository, ItemRepository itemRepository, AtorService atorService,
			DiretorService diretorService, ClasseService classeService) {
		this.repository = repository;
		this.itemRepository = itemRepository;
		this.atorService = atorService;
		this.diretorService = diretorService;
		this.classeService = classeService;
	}

	public List<Titulo> findAll() {
		return repository.findAll(Sort.by("nome"));
	}

	public Titulo findById(Long id) {
		return repository.findById(id)
				.orElseThrow(() -> new ObjetoNaoEncontradoException("Título de id " + id + " não encontrado"));
	}

	public Titulo insert(Titulo titulo) {
		Titulo novo = new Titulo();
		copiarDados(titulo, novo);
		return repository.save(novo);
	}

	public Titulo update(Long id, Titulo titulo) {
		Titulo existente = findById(id);
		copiarDados(titulo, existente);
		return repository.save(existente);
	}

	public void delete(Long id) {
		findById(id);
		if (itemRepository.existsByTituloId(id)) {
			throw new IntegridadeException("Não é possível excluir o título, pois ele possui itens cadastrados");
		}
		repository.deleteById(id);
	}

	/**
	 * O JSON recebido traz só os ids de atores, diretor e classe.
	 * Aqui buscamos cada um no banco (404 se não existir) e copiamos os demais campos.
	 */
	private void copiarDados(Titulo origem, Titulo destino) {
		destino.setNome(origem.getNome());
		destino.setAno(origem.getAno());
		destino.setSinopse(origem.getSinopse());
		destino.setCategoria(origem.getCategoria());

		List<Ator> atores = new ArrayList<>();
		for (Ator ator : origem.getAtores()) {
			if (ator == null || ator.getId() == null) {
				throw new IntegridadeException("Informe o id de cada ator do título");
			}
			Ator encontrado = atorService.findById(ator.getId());
			if (!atores.contains(encontrado)) {
				atores.add(encontrado);
			}
		}
		destino.setAtores(atores);
		if (origem.getDiretor().getId() == null || origem.getClasse().getId() == null) {
			throw new IntegridadeException("Informe o id do diretor e da classe do título");
		}
		destino.setDiretor(diretorService.findById(origem.getDiretor().getId()));
		destino.setClasse(classeService.findById(origem.getClasse().getId()));
	}

}
