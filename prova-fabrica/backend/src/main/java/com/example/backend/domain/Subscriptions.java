package com.example.backend.domain;

import java.time.LocalDate;

public record Subscriptions(
    String name,
    String email,
    LocalDate birthDate,
    Length length
) {
    
}
