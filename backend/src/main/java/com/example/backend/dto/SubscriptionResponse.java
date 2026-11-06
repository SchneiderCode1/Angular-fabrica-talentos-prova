package com.example.backend.dto;

import java.time.LocalDate;

import com.example.backend.domain.Categories;
import com.example.backend.domain.Length;
import com.example.backend.domain.Subscriptions;
import com.fasterxml.jackson.annotation.JsonProperty;

public record SubscriptionResponse(
    @JsonProperty("nome")           String name,
    @JsonProperty("email")          String email,
    @JsonProperty("dataNascimento") LocalDate birthDate,
    @JsonProperty("idadeNaProva")   int ageAtRace,
    @JsonProperty("percurso")       Length length,
    @JsonProperty("categoria")      Categories categorie
) {

    public static SubscriptionResponse de(Subscriptions subscription, int ageAtRace, Categories categorie) {
        return new SubscriptionResponse(
            subscription.name(),
            subscription.email(),
            subscription.birthDate(),
            ageAtRace,
            subscription.length(),
            categorie);
        }

}
