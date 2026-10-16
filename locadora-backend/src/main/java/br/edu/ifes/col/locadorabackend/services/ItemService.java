package br.edu.ifes.col.locadorabackend.services;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.edu.ifes.col.locadorabackend.domain.Item;
import br.edu.ifes.col.locadorabackend.domain.ItemRepository;
import br.edu.ifes.col.locadorabackend.exceptions.IntegridadeException;
import br.edu.ifes.col.locadorabackend.exceptions.ObjetoNaoEncontradoException;

@Service
public class ItemService {

	private final ItemRepository repository;
	private final TituloService tituloService;

	public ItemService(ItemRepository repository, TituloService tituloService) {
		this.repository = repository;
		this.tituloService = tituloService;
	}

	public List<Item> findAll() {
		return repository.findAll(Sort.by("numSerie"));
	}

	public Item findById(Long id) {
		return repository.findById(id)
				.orElseThrow(() -> new ObjetoNaoEncontradoException("Item de id " + id + " não encontrado"));
	}

	public Item insert(Item item) {
		if (repository.existsByNumSerie(item.getNumSerie())) {
			throw new IntegridadeException("Já existe um item com o número de série " + item.getNumSerie());
		}
		Item novo = new Item();
		copiarDados(item, novo);
		return repository.save(novo);
	}

	public Item update(Long id, Item item) {
		Item existente = findById(id);
		if (repository.existsByNumSerieAndIdNot(item.getNumSerie(), id)) {
			throw new IntegridadeException("Já existe um item com o número de série " + item.getNumSerie());
		}
		copiarDados(item, existente);
		return repository.save(existente);
	}

	public void delete(Long id) {
		findById(id);
		// Na Sprint 3, quando existir Locação, entra aqui a regra:
		// não excluir item que possua locações.
		repository.deleteById(id);
	}

	private void copiarDados(Item origem, Item destino) {
		destino.setNumSerie(origem.getNumSerie());
		destino.setDtAquisicao(origem.getDtAquisicao());
		destino.setTipoItem(origem.getTipoItem());
		if (origem.getTitulo().getId() == null) {
			throw new IntegridadeException("Informe o id do título do item");
		}
		destino.setTitulo(tituloService.findById(origem.getTitulo().getId()));
	}

}
