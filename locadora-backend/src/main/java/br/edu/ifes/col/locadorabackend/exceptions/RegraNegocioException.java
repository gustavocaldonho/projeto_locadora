package br.edu.ifes.col.locadorabackend.exceptions;

/**
 * Lançada quando uma regra do negócio impede a operação, por exemplo
 * locar para um cliente em débito ou cancelar uma locação já paga. Vira HTTP 400.
 */
public class RegraNegocioException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RegraNegocioException(String mensagem) {
		super(mensagem);
	}

}
