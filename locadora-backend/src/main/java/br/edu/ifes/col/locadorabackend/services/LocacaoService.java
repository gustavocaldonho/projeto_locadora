package br.edu.ifes.col.locadorabackend.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ifes.col.locadorabackend.domain.Cliente;
import br.edu.ifes.col.locadorabackend.domain.Item;
import br.edu.ifes.col.locadorabackend.domain.Locacao;
import br.edu.ifes.col.locadorabackend.dto.DevolucaoInfo;
import br.edu.ifes.col.locadorabackend.dto.LocacaoRequest;
import br.edu.ifes.col.locadorabackend.exceptions.ObjetoNaoEncontradoException;
import br.edu.ifes.col.locadorabackend.exceptions.RegraNegocioException;
import br.edu.ifes.col.locadorabackend.repositories.ItemRepository;
import br.edu.ifes.col.locadorabackend.repositories.LocacaoRepository;

@Service
public class LocacaoService {

	private static final DateTimeFormatter DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	private final LocacaoRepository repository;
	private final ItemRepository itemRepository;
	private final ItemService itemService;
	private final ClienteService clienteService;

	public LocacaoService(LocacaoRepository repository, ItemRepository itemRepository, ItemService itemService,
			ClienteService clienteService) {
		this.repository = repository;
		this.itemRepository = itemRepository;
		this.itemService = itemService;
		this.clienteService = clienteService;
	}

	public List<Locacao> findAll() {
		return repository.findAllByOrderByDtLocacaoDescIdDesc();
	}

	public List<Locacao> findVigentes() {
		return repository.findByDtDevolucaoEfetivaIsNullOrderByDtDevolucaoPrevista();
	}

	public Locacao findById(Long id) {
		return repository.findById(id)
				.orElseThrow(() -> new ObjetoNaoEncontradoException("Locação de id " + id + " não encontrada"));
	}

	/** Itens que não têm locação vigente, ou seja, que podem ser locados agora. */
	public List<Item> findItensDisponiveis() {
		return itemService.findAll().stream()
				.filter(item -> repository.findByItemIdAndDtDevolucaoEfetivaIsNull(item.getId()).isEmpty())
				.collect(Collectors.toList());
	}

	/**
	 * Efetuar nova locação. O valor e a data prevista vêm da classe do título,
	 * a não ser que o funcionário informe outros.
	 */
	public Locacao insert(LocacaoRequest dados) {
		Cliente cliente = clienteService.findById(dados.clienteId());
		Item item = itemService.findById(dados.itemId());
		LocalDate hoje = LocalDate.now();

		if (!cliente.getEstahAtivo()) {
			throw new RegraNegocioException("O cliente " + cliente.getNome() + " está inativo");
		}
		verificarDebito(cliente, hoje);
		repository.findByItemIdAndDtDevolucaoEfetivaIsNull(item.getId()).ifPresent(vigente -> {
			throw new RegraNegocioException("O item " + item.getNumSerie() + " já está locado. Devolução prevista para "
					+ vigente.getDtDevolucaoPrevista().format(DATA));
		});

		Locacao locacao = new Locacao();
		locacao.setCliente(cliente);
		locacao.setItem(item);
		locacao.setDtLocacao(hoje);
		locacao.setValorCobrado(dados.valorCobrado() != null ? dados.valorCobrado()
				: item.getTitulo().getClasse().getValor());
		locacao.setDtDevolucaoPrevista(dados.dtDevolucaoPrevista() != null ? dados.dtDevolucaoPrevista()
				: hoje.plusDays(item.getTitulo().getClasse().getPrazoDevolucao()));
		locacao.setPaga(Boolean.TRUE.equals(dados.paga()));
		validarDataPrevista(locacao);
		return repository.save(locacao);
	}

	/** Alterar dados de locação: valor, data prevista e se já foi paga. Item e cliente não mudam. */
	public Locacao update(Long id, LocacaoRequest dados) {
		Locacao locacao = findById(id);
		if (!locacao.isVigente()) {
			throw new RegraNegocioException("Não é possível alterar uma locação já devolvida");
		}
		if (dados.valorCobrado() != null) {
			locacao.setValorCobrado(dados.valorCobrado());
		}
		if (dados.dtDevolucaoPrevista() != null) {
			locacao.setDtDevolucaoPrevista(dados.dtDevolucaoPrevista());
		}
		if (dados.paga() != null) {
			locacao.setPaga(dados.paga());
		}
		validarDataPrevista(locacao);
		return repository.save(locacao);
	}

	/** Cancelar locação: só locações não pagas. A locação é excluída. */
	public void cancelar(Long id) {
		Locacao locacao = findById(id);
		if (locacao.getPaga()) {
			throw new RegraNegocioException("A locação já possui pagamento e, portanto, não pode ser cancelada");
		}
		repository.delete(locacao);
	}

	/** Mostra quanto o cliente vai pagar, sem registrar a devolução. */
	public DevolucaoInfo consultarDevolucao(String numSerie) {
		Locacao locacao = buscarLocacaoVigente(numSerie);
		return calcularDevolucao(locacao, LocalDate.now());
	}

	/** Efetuar devolução: registra a data efetiva, a multa (se houver) e marca como paga. */
	@Transactional
	public DevolucaoInfo devolver(String numSerie) {
		Locacao locacao = buscarLocacaoVigente(numSerie);
		LocalDate hoje = LocalDate.now();
		DevolucaoInfo info = calcularDevolucao(locacao, hoje);
		locacao.setDtDevolucaoEfetiva(hoje);
		locacao.setMultaCobrada(info.multa());
		locacao.setPaga(true);
		repository.save(locacao);
		return info;
	}

	/**
	 * Regra da multa (o documento de análise não define uma):
	 * cada dia de atraso custa o valor da classe do título.
	 */
	public double calcularMulta(Locacao locacao, LocalDate dataDevolucao) {
		long diasAtraso = diasAtraso(locacao, dataDevolucao);
		return diasAtraso * locacao.getItem().getTitulo().getClasse().getValor();
	}

	private long diasAtraso(Locacao locacao, LocalDate dataDevolucao) {
		return Math.max(0, ChronoUnit.DAYS.between(locacao.getDtDevolucaoPrevista(), dataDevolucao));
	}

	private DevolucaoInfo calcularDevolucao(Locacao locacao, LocalDate dataDevolucao) {
		long dias = diasAtraso(locacao, dataDevolucao);
		double multa = calcularMulta(locacao, dataDevolucao);
		double valorLocacao = locacao.getPaga() ? 0 : locacao.getValorCobrado();
		return new DevolucaoInfo(locacao, dias, valorLocacao, multa, valorLocacao + multa);
	}

	private Locacao buscarLocacaoVigente(String numSerie) {
		if (!itemRepository.existsByNumSerie(numSerie)) {
			throw new ObjetoNaoEncontradoException("Não existe item com o número de série " + numSerie);
		}
		return repository.findByItemNumSerieAndDtDevolucaoEfetivaIsNull(numSerie)
				.orElseThrow(() -> new RegraNegocioException("O item " + numSerie + " não está locado no momento"));
	}

	/** Cliente em débito = tem locação não devolvida com a data prevista já vencida. */
	private void verificarDebito(Cliente cliente, LocalDate hoje) {
		List<Locacao> atrasadas = repository
				.findByClienteNumInscricaoAndDtDevolucaoEfetivaIsNullAndDtDevolucaoPrevistaBefore(
						cliente.getNumInscricao(), hoje);
		if (!atrasadas.isEmpty()) {
			String lista = atrasadas.stream()
					.map(l -> l.getItem().getTitulo().getNome() + " (item " + l.getItem().getNumSerie()
							+ ", previsto para " + l.getDtDevolucaoPrevista().format(DATA) + ")")
					.collect(Collectors.joining("; "));
			throw new RegraNegocioException("O cliente " + cliente.getNome() + " está em débito. Locações em atraso: "
					+ lista);
		}
	}

	private void validarDataPrevista(Locacao locacao) {
		if (!locacao.getDtDevolucaoPrevista().isAfter(locacao.getDtLocacao())) {
			throw new RegraNegocioException("A data de devolução prevista deve ser maior que a data da locação");
		}
	}

}
