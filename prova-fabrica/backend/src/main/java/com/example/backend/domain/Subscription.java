package com.example.backend.domain;

import java.time.LocalDate;

public record Subscription(
    String name,
    String email,
    LocalDate birthDate,
    Length length
) {
    
}
