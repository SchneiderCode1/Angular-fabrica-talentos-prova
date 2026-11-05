package com.example.backend.exception;

public class SubscriptionNotFoundException extends RuntimeException {
    public SubscriptionNotFoundException(String email) {
        super("Não existe uma inscrição com o seguinte email: " + email);
    }
}
