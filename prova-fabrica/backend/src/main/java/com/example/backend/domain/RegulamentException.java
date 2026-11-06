package com.example.backend.domain;

public enum RegulamentException {
    MIN_AGE(
        "A idade mínima para participar é: " + SubscriptionRules.MIN_AGE + " anos."
    ),
    DISTANCE_NOT_PERMITED(
        "A idade mínima para participar da corrida de 10km é: " + SubscriptionRules.MIN_AGE_10KM + " anos."
    );

    private final String message;

    RegulamentException(String message) {
        this.message = message;
    };

    public String getMessage() {
        return message;
    };

};
