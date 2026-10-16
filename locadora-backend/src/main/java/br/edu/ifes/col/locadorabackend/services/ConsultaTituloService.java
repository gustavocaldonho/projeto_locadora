package br.edu.ifes.col.locadorabackend.services;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import br.edu.ifes.col.locadorabackend.domain.Ator;
import br.edu.ifes.col.locadorabackend.domain.Locacao;
import br.edu.ifes.col.locadorabackend.domain.Titulo;
import br.edu.ifes.col.locadorabackend.dto.TituloConsulta;
import br.edu.ifes.col.locadorabackend.repositories.AtorRepository;
import br.edu.ifes.col.locadorabackend.repositories.ItemRepository;
import br.edu.ifes.col.locadorabackend.repositories.LocacaoRepository;
import br.edu.ifes.col.locadorabackend.repositories.TituloRepository;

/**
 * Caso de uso Consultar Título: busca por nome, por categoria e por ator,
 * mostrando a quantidade de cópias disponíveis.
 */
@Service
public class ConsultaTituloService {

	private final TituloRepository tituloRepository;
	private final AtorRepository atorRepository;
	private final ItemRepository itemRepository;
	private final LocacaoRepository locacaoRepository;

	public ConsultaTituloService(TituloRepository tituloRepository, AtorRepository atorRepository,
			ItemRepository itemRepository, LocacaoRepository locacaoRepository) {
		this.tituloRepository = tituloRepository;
		this.atorRepository = atorRepository;
		this.itemRepository = itemRepository;
		this.locacaoRepository = locacaoRepository;
	}

	/**
	 * Os três filtros são opcionais e podem ser combinados.
	 * O nome é buscado em qualquer parte, sem diferenciar maiúsculas e minúsculas.
	 */
	public List<TituloConsulta> consultar(String nome, String categoria, Long atorId) {
		return tituloRepository.findAll().stream()
				.filter(t -> nome == null || nome.isBlank() || t.getNome().toLowerCase().contains(nome.trim().toLowerCase()))
				.filter(t -> categoria == null || categoria.isBlank() || t.getCategoria().equalsIgnoreCase(categoria.trim()))
				.filter(t -> atorId == null || t.getAtores().stream().anyMatch(a -> a.getId().equals(atorId)))
				.sorted(Comparator.comparing(Titulo::getNome, String.CASE_INSENSITIVE_ORDER))
				.map(this::montar)
				.toList();
	}

	/** Categorias existentes, para a lista de opções da consulta. */
	public List<String> categorias() {
		return tituloRepository.findAll().stream()
				.map(Titulo::getCategoria)
				.filter(Objects::nonNull)
				.distinct()
				.sorted(String.CASE_INSENSITIVE_ORDER)
				.toList();
	}

	/** Atores, para a lista de opções da consulta. */
	public List<Ator> atores() {
		return atorRepository.findAll().stream()
				.sorted(Comparator.comparing(Ator::getNome, String.CASE_INSENSITIVE_ORDER))
				.toList();
	}

	private TituloConsulta montar(Titulo titulo) {
		long total = itemRepository.countByTituloId(titulo.getId());
		List<Locacao> vigentes = locacaoRepository.findByItemTituloIdAndDtDevolucaoEfetivaIsNull(titulo.getId());
		long disponiveis = total - vigentes.size();
		LocalDate previsao = null;
		if (total > 0 && disponiveis == 0) {
			previsao = vigentes.stream()
					.map(Locacao::getDtDevolucaoPrevista)
					.min(Comparator.naturalOrder())
					.orElse(null);
		}
		return new TituloConsulta(
				titulo.getId(),
				titulo.getNome(),
				titulo.getAno(),
				titulo.getCategoria(),
				titulo.getSinopse(),
				titulo.getDiretor().getNome(),
				titulo.getAtores().stream().map(Ator::getNome).sorted().toList(),
				titulo.getClasse().getNome(),
				titulo.getClasse().getValor(),
				titulo.getClasse().getPrazoDevolucao(),
				total,
				disponiveis,
				previsao);
	}

}
