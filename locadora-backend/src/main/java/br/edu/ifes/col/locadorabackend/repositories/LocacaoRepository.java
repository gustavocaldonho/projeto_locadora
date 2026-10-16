package br.edu.ifes.col.locadorabackend.repositories;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifes.col.locadorabackend.domain.Locacao;

public interface LocacaoRepository extends JpaRepository<Locacao, Long> {

	List<Locacao> findAllByOrderByDtLocacaoDescIdDesc();

	List<Locacao> findByDtDevolucaoEfetivaIsNullOrderByDtDevolucaoPrevista();

	/** Locação vigente do item (só pode existir uma). */
	Optional<Locacao> findByItemIdAndDtDevolucaoEfetivaIsNull(Long itemId);

	Optional<Locacao> findByItemNumSerieAndDtDevolucaoEfetivaIsNull(String numSerie);

	/** Locações em atraso do cliente: não devolvidas e com a data prevista já vencida. */
	List<Locacao> findByClienteNumInscricaoAndDtDevolucaoEfetivaIsNullAndDtDevolucaoPrevistaBefore(Long clienteId,
			LocalDate data);

	/** Locações vigentes (não devolvidas) das cópias de um título. */
	List<Locacao> findByItemTituloIdAndDtDevolucaoEfetivaIsNull(Long tituloId);

	boolean existsByItemId(Long itemId);

	boolean existsByClienteNumInscricaoIn(Collection<Long> clienteIds);

}
