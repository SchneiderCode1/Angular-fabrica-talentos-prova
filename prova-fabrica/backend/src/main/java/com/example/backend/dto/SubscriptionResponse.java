package com.example.backend.dto;

import java.time.LocalDate;

import com.example.backend.domain.Categories;
import com.example.backend.domain.Length;
import com.example.backend.domain.Subscriptions;

public record SubscriptionResponse(
    String name,
    String email,
    LocalDate birthDate,
    int ageAtRace,
    Length length,
    Categories categorie
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
