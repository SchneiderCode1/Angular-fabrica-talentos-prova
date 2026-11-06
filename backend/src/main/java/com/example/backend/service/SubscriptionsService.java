package com.example.backend.service;

import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;

import com.example.backend.domain.Length;
import org.springframework.stereotype.Service;

import com.example.backend.domain.SubscriptionEval;
import com.example.backend.domain.SubscriptionRules;
import com.example.backend.domain.Subscriptions;
import com.example.backend.dto.EditSubscriptionRequest;
import com.example.backend.dto.ShortResponse;
import com.example.backend.dto.SubscriptionRequest;
import com.example.backend.dto.SubscriptionResponse;
import com.example.backend.exception.SubscriptionEmailAlreadyExistsException;
import com.example.backend.exception.SubscriptionNotFoundException;
import com.example.backend.exception.SubscriptionNotValidException;
import com.example.backend.repository.SubscriptionsRepository;

@Service
public class SubscriptionsService {

    private static final int MIN_NAME_LENGTH = 3;
    private static final int MAX_NAME_LENGTH = 100;
    private static final Pattern NAME_FORMAT_PERMITTED = Pattern.compile("^[\\p{L}'’ -]+$");
    private static final Pattern EMAIL_FORMAT_PERMITTED = Pattern.compile("^[a-zA-Z0-9._%+-]{2,}@[a-zA-Z0-9-]{2,}(?:\\.[a-zA-Z]{2,})+$");

    private final SubscriptionsRepository repository;

    public SubscriptionsService(SubscriptionsRepository repository) {
        this.repository = repository;
    }

    public SubscriptionResponse register(SubscriptionRequest request) {
        String name = validateName(request.name());
        String email = validateEmail(request.email());
        validateRegulament(request.birthDate(), request.length());

        if (repository.existsByEmail(email)) {
            throw new SubscriptionEmailAlreadyExistsException(email);
        }

        Subscriptions subscription = new Subscriptions(name, email, request.birthDate(), request.length());
        repository.save(subscription);
        return transformToResponse(subscription);
    }

    public List<SubscriptionResponse> list() {
        return repository.list().stream()
                .map(this::transformToResponse)
                .toList();
    }

    public void cancel(String email) {
        String normalizedEmail = SubscriptionRules.normalizeEmail(email);
        if (!repository.remove(normalizedEmail)) {
            throw new SubscriptionNotFoundException(normalizedEmail);
        }
    }

    public ShortResponse summary() {
        List<Subscriptions> subscriptions = repository.list();
        long km5 = subscriptions.stream().filter(s -> s.length() == Length.KM_5).count();
        long km10 = subscriptions.stream().filter(s -> s.length() == Length.KM_10).count();
        return new ShortResponse(subscriptions.size(), km5, km10);
    }

    public SubscriptionResponse check(String email) {
        return transformToResponse(searchExistent(email));
    }

    public SubscriptionResponse edit(String email, EditSubscriptionRequest request) {
        Subscriptions current_subscription = searchExistent(email);

        String name = validateName(request.name());
        validateRegulament(request.birthDate(), request.length());

        Subscriptions updated_subscription = new Subscriptions(name, current_subscription.email(), request.birthDate(), request.length());
        repository.save(updated_subscription);
        return transformToResponse(updated_subscription);
    }

    private Subscriptions searchExistent(String email) {
        String normalizedEmail = SubscriptionRules.normalizeEmail(email);
        return repository.searchByEmail(normalizedEmail).orElseThrow(() -> new SubscriptionNotFoundException(normalizedEmail));
    }

    private String validateName (String curr_name) {
        String name = SubscriptionRules.normalizeName(curr_name);
        if (name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH) {
            throw new SubscriptionNotValidException(
                "O nome deve ter entre " + MIN_NAME_LENGTH + " e " + MAX_NAME_LENGTH + " caracteres.");
        }

        if (!NAME_FORMAT_PERMITTED.matcher(name).matches()) {
            throw new SubscriptionNotValidException(
                "O nome deve conter apenas letras.");
        }
        return name;
    }

    private String validateEmail (String curr_email) {
        String email = SubscriptionRules.normalizeEmail(curr_email);
        if (!EMAIL_FORMAT_PERMITTED.matcher(email).matches()) {
            throw new SubscriptionNotValidException(
                "O formato de email está incorreto, tente novamente.");
        }
        return email;
    }

    private void validateRegulament(LocalDate birthDate, Length length) {
        SubscriptionEval eval = SubscriptionRules.eval(birthDate, length, SubscriptionRules.RACE_DATE);
        if (!eval.accepted()) {
            throw new SubscriptionNotValidException(eval.error());
        }
    }

    private SubscriptionResponse transformToResponse(Subscriptions subscription) {
        int age = SubscriptionRules.calculateAge(subscription.birthDate(), SubscriptionRules.RACE_DATE);
        return SubscriptionResponse.de(subscription, age, SubscriptionRules.getCategorie(age));
    }
 }
