package com.achadoseperdidos.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ProblemDetail> tratarRecursoNaoEncontrado(RecursoNaoEncontradoException exception) {
        return resposta(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(RegraDeNegocioException.class)
    public ResponseEntity<ProblemDetail> tratarRegraDeNegocio(RegraDeNegocioException exception) {
        return resposta(HttpStatus.CONFLICT, exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> tratarValidacao(MethodArgumentNotValidException exception) {
        Map<String, String> erros = exception.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        erro -> erro.getDefaultMessage() == null ? "Valor inválido." : erro.getDefaultMessage(),
                        (mensagemExistente, mensagemNova) -> mensagemExistente
                ));

        ProblemDetail problema = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Um ou mais campos enviados são inválidos."
        );
        problema.setTitle("Falha de validação");
        problema.setProperty("erros", erros);
        return ResponseEntity.badRequest().body(problema);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProblemDetail> tratarJsonInvalido(HttpMessageNotReadableException exception) {
        return resposta(HttpStatus.BAD_REQUEST, "O JSON enviado é inválido ou contém um valor não reconhecido.");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetail> tratarIntegridade(DataIntegrityViolationException exception) {
        return resposta(HttpStatus.CONFLICT, "A operação viola uma regra de integridade dos dados.");
    }

    private ResponseEntity<ProblemDetail> resposta(HttpStatus status, String detalhe) {
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(status, detalhe);
        problema.setTitle(status.getReasonPhrase());
        return ResponseEntity.status(status).body(problema);
    }
}
