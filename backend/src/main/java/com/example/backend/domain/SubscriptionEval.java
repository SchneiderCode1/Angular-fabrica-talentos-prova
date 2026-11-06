package com.example.backend.domain;

public record SubscriptionEval(
    boolean accepted,
    int ageAtRace,
    Categories categorie,
    RegulamentException error
) {
    static SubscriptionEval accepted(int ageAtRace, Categories categorie) {
        return new SubscriptionEval(true, ageAtRace, categorie, null);
    };

    static SubscriptionEval refused(int ageAtRace, RegulamentException error) {
        return new SubscriptionEval(false, ageAtRace, null, error);
    };
}   