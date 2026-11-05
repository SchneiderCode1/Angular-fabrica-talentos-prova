package com.example.backend.dto;

import java.time.LocalDate;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

public record SubscriptionRequest(
    @NotBlank (message = "O campo de nome é obrigatório")
    String name,

    @NotBlank (message = "O campo de email é obrigatório")
    String email,

    @NotNull (message = "o campo de data de nascimento é obrigatório")
    @Past (message = "A data de nascimento deve ser anterior a hoje")
    LocalDate birDate,

    @NotNull (message = "O campo de percurso é obrigatório")
    Length length
) {
    
}
