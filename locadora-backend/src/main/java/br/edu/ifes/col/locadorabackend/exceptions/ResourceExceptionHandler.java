package br.edu.ifes.col.locadorabackend.exceptions;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Captura as exceções lançadas pelos resources e devolve um ErroPadrao
 * com o status HTTP adequado, em vez do erro 500 genérico.
 */
@ControllerAdvice
public class ResourceExceptionHandler {

	@ExceptionHandler(ObjetoNaoEncontradoException.class)
	public ResponseEntity<ErroPadrao> objetoNaoEncontrado(ObjetoNaoEncontradoException e, HttpServletRequest request) {
		return erro(HttpStatus.NOT_FOUND, "Não encontrado", e.getMessage(), request);
	}

	@ExceptionHandler(IntegridadeException.class)
	public ResponseEntity<ErroPadrao> integridade(IntegridadeException e, HttpServletRequest request) {
		return erro(HttpStatus.BAD_REQUEST, "Violação de integridade", e.getMessage(), request);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErroPadrao> validacao(MethodArgumentNotValidException e, HttpServletRequest request) {
		String mensagem = e.getBindingResult().getFieldErrors().stream()
				.map(f -> f.getDefaultMessage())
				.collect(Collectors.joining("; "));
		return erro(HttpStatus.BAD_REQUEST, "Dados inválidos", mensagem, request);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErroPadrao> jsonInvalido(HttpMessageNotReadableException e, HttpServletRequest request) {
		return erro(HttpStatus.BAD_REQUEST, "Requisição inválida",
				"O corpo da requisição tem um formato inválido (verifique datas, números e o tipo do item)", request);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErroPadrao> parametroInvalido(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
		return erro(HttpStatus.BAD_REQUEST, "Requisição inválida",
				"Valor inválido para o parâmetro '" + e.getName() + "'", request);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ErroPadrao> bancoDeDados(DataIntegrityViolationException e, HttpServletRequest request) {
		return erro(HttpStatus.BAD_REQUEST, "Violação de integridade",
				"A operação viola uma restrição do banco de dados", request);
	}

	private ResponseEntity<ErroPadrao> erro(HttpStatus status, String error, String message, HttpServletRequest request) {
		ErroPadrao erro = new ErroPadrao(LocalDateTime.now(), status.value(), error, message, request.getRequestURI());
		return ResponseEntity.status(status).body(erro);
	}

}
