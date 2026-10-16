package br.edu.ifes.col.locadorabackend.exceptions;

/**
 * Lançada quando o registro procurado não existe. Vira HTTP 404.
 */
public class ObjetoNaoEncontradoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ObjetoNaoEncontradoException(String mensagem) {
		super(mensagem);
	}

}
