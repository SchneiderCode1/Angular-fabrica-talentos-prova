package com.example.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ShortResponse(
    @JsonProperty ("total") long total, long km5, long km10) { // Esse resumo está faltando até eu descobrir como utilizar List<Subscription>
}
