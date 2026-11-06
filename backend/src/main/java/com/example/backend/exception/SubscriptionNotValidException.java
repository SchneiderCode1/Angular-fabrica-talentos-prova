package com.example.backend.exception;

import com.example.backend.domain.RegulamentException;

public class SubscriptionNotValidException extends RuntimeException {
    public SubscriptionNotValidException(String string) {
        super("A inscrição está inválida, cheque o formato de email.");
    }

    public SubscriptionNotValidException(RegulamentException error) {
        super(error.getMessage());
    }

    // o java permite usar multiplos construtores contanto que as entradas sejam diferentes: nesse caso a primeira é nula a seguda é a chamada de erro do domínio
}
