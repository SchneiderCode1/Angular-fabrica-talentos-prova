package com.example.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ExceptionResponse(
    @JsonProperty  ("mensagem") String message
) {
}
