package com.example.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.EditSubscriptionRequest;
import com.example.backend.dto.ShortResponse;
import com.example.backend.dto.SubscriptionRequest;
import com.example.backend.dto.SubscriptionResponse;
import com.example.backend.service.SubscriptionsService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/inscricoes")
public class SubscriptionsController {

    private final SubscriptionsService service;

    public SubscriptionsController(SubscriptionsService service) {
        this.service = service;
    }

    @PostMapping("/registrar")
    public ResponseEntity<SubscriptionResponse> register(@Valid @RequestBody SubscriptionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.register(request));
    }

    @GetMapping("/listar")
    public List<SubscriptionResponse> list() {
        return service.list();
    }

    @GetMapping("/resumo")
    public ShortResponse summary() {
        return service.summary();
    }

    @GetMapping("/{email}")
    public SubscriptionResponse check(@PathVariable("email") String email) {
        return service.check(email);
    }

    @PutMapping("/{email}")
    public SubscriptionResponse edit(@PathVariable("email") String email,
                                    @Valid @RequestBody EditSubscriptionRequest request) {
        return service.edit(email, request);
    }

    @DeleteMapping("/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancel(@PathVariable("email") String email) {
        service.cancel(email);
    }
}
