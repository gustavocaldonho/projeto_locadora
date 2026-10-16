package br.edu.ifes.col.locadorabackend.exceptions;

/**
 * Lançada quando uma operação viola uma regra de integridade do sistema,
 * por exemplo excluir um ator que participa de algum título. Vira HTTP 400.
 */
public class IntegridadeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public IntegridadeException(String mensagem) {
		super(mensagem);
	}

}
