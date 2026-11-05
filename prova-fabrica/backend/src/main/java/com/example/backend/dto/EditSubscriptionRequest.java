package com.example.backend.dto;

import com.example.backend.domain.Length;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

public record EditSubscriptionRequest(
    @NotBlank (message = "O campo de nome é obrigatório durante a edição")
    String name,

    @NotNull (message = "o campo de data de nascimento é obrigatório durante a edição")
    @Past (message = "A data de nascimento deve ser anterior a hoje")
    LocalDate birthDate,

    @NotNull (message = "O campo de percurso é obrigatório durante a edição")
    Length length
) {
    
}
