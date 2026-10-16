package br.edu.ifes.col.locadorabackend.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ifes.col.locadorabackend.domain.Dependente;
import br.edu.ifes.col.locadorabackend.domain.Socio;
import br.edu.ifes.col.locadorabackend.exceptions.IntegridadeException;
import br.edu.ifes.col.locadorabackend.exceptions.ObjetoNaoEncontradoException;
import br.edu.ifes.col.locadorabackend.repositories.DependenteRepository;
import br.edu.ifes.col.locadorabackend.repositories.LocacaoRepository;
import br.edu.ifes.col.locadorabackend.repositories.SocioRepository;

@Service
public class SocioService {

	/** Um sócio pode ter no máximo 3 dependentes ativos ao mesmo tempo. */
	public static final int MAX_DEPENDENTES_ATIVOS = 3;

	private final SocioRepository repository;
	private final DependenteRepository dependenteRepository;
	private final LocacaoRepository locacaoRepository;

	public SocioService(SocioRepository repository, DependenteRepository dependenteRepository,
			LocacaoRepository locacaoRepository) {
		this.repository = repository;
		this.dependenteRepository = dependenteRepository;
		this.locacaoRepository = locacaoRepository;
	}

	public List<Socio> findAll() {
		return repository.findAll(Sort.by("nome"));
	}

	public Socio findById(Long id) {
		return repository.findById(id)
				.orElseThrow(() -> new ObjetoNaoEncontradoException("Sócio de inscrição " + id + " não encontrado"));
	}

	public List<Dependente> findDependentes(Long id) {
		findById(id);
		return dependenteRepository.findBySocioNumInscricaoOrderByNumInscricao(id);
	}

	/** Inscrever novo sócio: o número de inscrição é gerado e o sócio começa ativo. */
	public Socio insert(Socio socio) {
		if (repository.existsByCpf(socio.getCpf())) {
			throw new IntegridadeException("Já existe um sócio com o CPF " + socio.getCpf());
		}
		socio.setNumInscricao(null);
		socio.setEstahAtivo(true);
		return repository.save(socio);
	}

	/** Alterar dados: o número de inscrição e a situação (ativo) não mudam por aqui. */
	public Socio update(Long id, Socio socio) {
		Socio existente = findById(id);
		if (repository.existsByCpfAndNumInscricaoNot(socio.getCpf(), id)) {
			throw new IntegridadeException("Já existe um sócio com o CPF " + socio.getCpf());
		}
		existente.setNome(socio.getNome());
		existente.setDtNascimento(socio.getDtNascimento());
		existente.setSexo(socio.getSexo());
		existente.setCpf(socio.getCpf());
		existente.setEndereco(socio.getEndereco());
		existente.setTel(socio.getTel());
		return repository.save(existente);
	}

	/** Desativar sócio: ele e todos os seus dependentes ficam inativos. */
	@Transactional
	public Socio desativar(Long id) {
		Socio socio = findById(id);
		socio.setEstahAtivo(false);
		for (Dependente dependente : dependenteRepository.findBySocioNumInscricaoOrderByNumInscricao(id)) {
			dependente.setEstahAtivo(false);
		}
		return socio;
	}

	/** Reativar sócio: ele volta a ficar ativo, junto com até 3 dos seus dependentes. */
	@Transactional
	public Socio reativar(Long id) {
		Socio socio = findById(id);
		socio.setEstahAtivo(true);
		long ativos = dependenteRepository.countBySocioNumInscricaoAndEstahAtivoTrue(id);
		for (Dependente dependente : dependenteRepository.findBySocioNumInscricaoOrderByNumInscricao(id)) {
			if (!dependente.getEstahAtivo() && ativos < MAX_DEPENDENTES_ATIVOS) {
				dependente.setEstahAtivo(true);
				ativos++;
			}
		}
		return socio;
	}

	/** Excluir sócio: exclui também os dependentes. Não pode haver locações de nenhum deles. */
	@Transactional
	public void delete(Long id) {
		findById(id);
		List<Dependente> dependentes = dependenteRepository.findBySocioNumInscricaoOrderByNumInscricao(id);
		List<Long> ids = new ArrayList<>();
		ids.add(id);
		dependentes.forEach(d -> ids.add(d.getNumInscricao()));
		if (locacaoRepository.existsByClienteNumInscricaoIn(ids)) {
			throw new IntegridadeException(
					"Não é possível excluir o sócio, pois ele ou algum dependente possui locações");
		}
		dependenteRepository.deleteAll(dependentes);
		repository.deleteById(id);
	}

}
