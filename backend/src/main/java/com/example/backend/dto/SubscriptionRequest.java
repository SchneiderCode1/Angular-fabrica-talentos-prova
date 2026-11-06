package com.example.backend.dto;

import java.time.LocalDate;

import com.example.backend.domain.Length;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

public record SubscriptionRequest(
    @JsonProperty ("nome")
    @NotBlank (message = "O campo de nome é obrigatório")
    String name,

    @JsonProperty ("email")
    @NotBlank (message = "O campo de email é obrigatório")
    String email,

    @JsonProperty ("dataNascimento")
    @NotNull (message = "o campo de data de nascimento é obrigatório")
    @Past (message = "A data de nascimento deve ser anterior a hoje")
    LocalDate birthDate,

    @JsonProperty ("percurso")
    @NotNull (message = "O campo de percurso é obrigatório")
    Length length
) {
    
}
