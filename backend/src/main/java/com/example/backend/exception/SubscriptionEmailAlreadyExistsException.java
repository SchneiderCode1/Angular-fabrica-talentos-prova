package com.example.backend.exception;

public class SubscriptionEmailAlreadyExistsException extends RuntimeException {
    public  SubscriptionEmailAlreadyExistsException(String email) {
        super("Já existe uma inscrição com o seguinte email: " + email + ". Codigo 409.");
    }
}