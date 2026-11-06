package com.example.backend.dto;

import com.example.backend.domain.Length;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

public record EditSubscriptionRequest(
    @JsonProperty("nome")
    @NotBlank (message = "O campo de nome é obrigatório durante a edição")
    String name,

    @JsonProperty("dataNascimento")
    @NotNull (message = "o campo de data de nascimento é obrigatório durante a edição")
    @Past (message = "A data de nascimento deve ser anterior a hoje")
    LocalDate birthDate,

    @JsonProperty("percurso")
    @NotNull (message = "O campo de percurso é obrigatório durante a edição")
    Length length
) {
    
}
