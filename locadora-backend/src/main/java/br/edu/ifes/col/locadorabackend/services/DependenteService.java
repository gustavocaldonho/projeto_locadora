package br.edu.ifes.col.locadorabackend.services;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.edu.ifes.col.locadorabackend.domain.Dependente;
import br.edu.ifes.col.locadorabackend.domain.Socio;
import br.edu.ifes.col.locadorabackend.exceptions.IntegridadeException;
import br.edu.ifes.col.locadorabackend.exceptions.ObjetoNaoEncontradoException;
import br.edu.ifes.col.locadorabackend.exceptions.RegraNegocioException;
import br.edu.ifes.col.locadorabackend.repositories.DependenteRepository;
import br.edu.ifes.col.locadorabackend.repositories.LocacaoRepository;

@Service
public class DependenteService {

	private final DependenteRepository repository;
	private final LocacaoRepository locacaoRepository;
	private final SocioService socioService;

	public DependenteService(DependenteRepository repository, LocacaoRepository locacaoRepository,
			SocioService socioService) {
		this.repository = repository;
		this.locacaoRepository = locacaoRepository;
		this.socioService = socioService;
	}

	public List<Dependente> findAll() {
		return repository.findAll(Sort.by("nome"));
	}

	public Dependente findById(Long id) {
		return repository.findById(id)
				.orElseThrow(() -> new ObjetoNaoEncontradoException("Dependente de inscrição " + id + " não encontrado"));
	}

	/** Incluir dependente: o sócio precisa estar ativo e ter menos de 3 dependentes ativos. */
	public Dependente insert(Dependente dependente) {
		if (dependente.getSocio().getNumInscricao() == null) {
			throw new IntegridadeException("Informe o número de inscrição do sócio");
		}
		Socio socio = socioService.findById(dependente.getSocio().getNumInscricao());
		if (!socio.getEstahAtivo()) {
			throw new RegraNegocioException("Não é possível incluir dependente para um sócio inativo");
		}
		verificarLimiteDependentes(socio);
		dependente.setNumInscricao(null);
		dependente.setSocio(socio);
		dependente.setEstahAtivo(true);
		return repository.save(dependente);
	}

	/** Alterar dados: o sócio, o número de inscrição e a situação não mudam por aqui. */
	public Dependente update(Long id, Dependente dependente) {
		Dependente existente = findById(id);
		existente.setNome(dependente.getNome());
		existente.setDtNascimento(dependente.getDtNascimento());
		existente.setSexo(dependente.getSexo());
		return repository.save(existente);
	}

	/** Desativar dependente: só ele fica inativo. */
	public Dependente desativar(Long id) {
		Dependente dependente = findById(id);
		dependente.setEstahAtivo(false);
		return repository.save(dependente);
	}

	/** Reativar dependente: o sócio precisa estar ativo e com menos de 3 dependentes ativos. */
	public Dependente reativar(Long id) {
		Dependente dependente = findById(id);
		if (dependente.getEstahAtivo()) {
			return dependente;
		}
		if (!dependente.getSocio().getEstahAtivo()) {
			throw new RegraNegocioException("Reative o sócio antes de reativar o dependente");
		}
		verificarLimiteDependentes(dependente.getSocio());
		dependente.setEstahAtivo(true);
		return repository.save(dependente);
	}

	public void delete(Long id) {
		findById(id);
		if (locacaoRepository.existsByClienteNumInscricaoIn(List.of(id))) {
			throw new IntegridadeException("Não é possível excluir o dependente, pois ele possui locações");
		}
		repository.deleteById(id);
	}

	private void verificarLimiteDependentes(Socio socio) {
		long ativos = repository.countBySocioNumInscricaoAndEstahAtivoTrue(socio.getNumInscricao());
		if (ativos >= SocioService.MAX_DEPENDENTES_ATIVOS) {
			throw new RegraNegocioException("O sócio já possui " + SocioService.MAX_DEPENDENTES_ATIVOS
					+ " dependentes ativos");
		}
	}

}
