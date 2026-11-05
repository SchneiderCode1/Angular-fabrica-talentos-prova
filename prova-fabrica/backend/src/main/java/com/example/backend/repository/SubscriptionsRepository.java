package com.example.backend.repository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.backend.domain.Subscriptions;

@Repository 
public class SubscriptionsRepository {

    private final Map<String, Subscriptions> subscriptions = new LinkedHashMap<>();

    public synchronized void save(Subscriptions subscription) {
        subscriptions.put(subscription.email(), subscription);
    }

    public synchronized Optional<Subscriptions> searchByEmail(String email) {
        return Optional.ofNullable((subscriptions.get(email)));
    }

    public synchronized boolean existsByEmail(String email) {
        return subscriptions.containsKey(email);
    }

    public synchronized List<Subscriptions> list() {
        return List.copyOf(subscriptions.values());
    }

    public synchronized boolean remove(String email) {
        return subscriptions.remove(email)!= null;
    }

    
    
}
