package br.edu.ifes.col.locadorabackend.domain;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * Locação de um item para um cliente.
 * Enquanto dtDevolucaoEfetiva for nula, a locação está vigente.
 */
@Entity
public class Locacao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private LocalDate dtLocacao;

	private LocalDate dtDevolucaoPrevista;

	private LocalDate dtDevolucaoEfetiva;

	private Double valorCobrado;

	private Double multaCobrada;

	/**
	 * O documento fala em "locação paga", mas o diagrama não tem esse campo.
	 * A locação fica paga quando o cliente paga na retirada ou na devolução.
	 */
	private Boolean paga = false;

	@ManyToOne
	@JoinColumn(name = "item_id")
	private Item item;

	@ManyToOne
	@JoinColumn(name = "cliente_id")
	private Cliente cliente;

	public boolean isVigente() {
		return dtDevolucaoEfetiva == null;
	}

	/** VIGENTE, ATRASADA ou DEVOLVIDA (calculado, não é gravado no banco). */
	public String getSituacao() {
		if (!isVigente()) {
			return "DEVOLVIDA";
		}
		return LocalDate.now().isAfter(dtDevolucaoPrevista) ? "ATRASADA" : "VIGENTE";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getDtLocacao() {
		return dtLocacao;
	}

	public void setDtLocacao(LocalDate dtLocacao) {
		this.dtLocacao = dtLocacao;
	}

	public LocalDate getDtDevolucaoPrevista() {
		return dtDevolucaoPrevista;
	}

	public void setDtDevolucaoPrevista(LocalDate dtDevolucaoPrevista) {
		this.dtDevolucaoPrevista = dtDevolucaoPrevista;
	}

	public LocalDate getDtDevolucaoEfetiva() {
		return dtDevolucaoEfetiva;
	}

	public void setDtDevolucaoEfetiva(LocalDate dtDevolucaoEfetiva) {
		this.dtDevolucaoEfetiva = dtDevolucaoEfetiva;
	}

	public Double getValorCobrado() {
		return valorCobrado;
	}

	public void setValorCobrado(Double valorCobrado) {
		this.valorCobrado = valorCobrado;
	}

	public Double getMultaCobrada() {
		return multaCobrada;
	}

	public void setMultaCobrada(Double multaCobrada) {
		this.multaCobrada = multaCobrada;
	}

	public Boolean getPaga() {
		return paga;
	}

	public void setPaga(Boolean paga) {
		this.paga = paga;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

}
