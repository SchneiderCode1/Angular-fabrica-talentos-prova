package com.example.backend.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.backend.dto.ExceptionResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(SubscriptionNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFound(SubscriptionNotFoundException ex) {
        return respond(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(SubscriptionEmailAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleEmailAlreadyExists(SubscriptionEmailAlreadyExistsException ex) {
        return respond(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(SubscriptionNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleNotValid(SubscriptionNotValidException ex) {
        return respond(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidField(MethodArgumentNotValidException ex) {
        FieldError error = ex.getBindingResult().getFieldError();
        String message = (error != null) ? error.getDefaultMessage() : "Dados inválidos.";
        return respond(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionResponse> handleUnreadableBody(HttpMessageNotReadableException ex) {
        return respond(HttpStatus.BAD_REQUEST,
                "Corpo da requisição inválido. Verifique o JSON, a data no formato aaaa-mm-dd "
                        + "e o percurso (KM_5 ou KM_10).");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleUnexpected(Exception ex) {
        if (ex instanceof ErrorResponse springError) {
            return ResponseEntity.status(springError.getStatusCode())
                    .body(new ExceptionResponse("Requisição não suportada por esta API."));
        }
        log.error("Erro inesperado", ex);
        return respond(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno. Tente novamente mais tarde.");
    }

    private ResponseEntity<ExceptionResponse> respond(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(new ExceptionResponse(message));
    }
}