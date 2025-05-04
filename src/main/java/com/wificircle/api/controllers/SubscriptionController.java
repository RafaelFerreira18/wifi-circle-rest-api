package com.wificircle.api.controllers;

import com.wificircle.api.models.Subscription;
import com.wificircle.api.repositories.SubscriptionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController {

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionController(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @PostMapping
    public ResponseEntity<Subscription> createSubscription(@RequestBody Subscription subscription) {
        try {
            Subscription saved = subscriptionRepository.save(subscription);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Subscription>> getAllSubscriptions() {
        try {
            List<Subscription> subscriptions = subscriptionRepository.findAll();
            return ResponseEntity.ok(subscriptions);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Subscription> getSubscriptionById(@PathVariable String id) {
        try {
            Optional<Subscription> subscription = subscriptionRepository.findById(id);
            return subscription.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Subscription> getSubscriptionByUserId(@PathVariable String userId) {
        try {
            Optional<Subscription> subscription = subscriptionRepository.findByUserId(userId);
            return subscription.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Subscription> updateSubscription(@PathVariable String id, @RequestBody Subscription subscription) {
        subscription.setId(id);
        try {
            Optional<Subscription> existing = subscriptionRepository.findById(id);
            if (existing.isEmpty()) return ResponseEntity.notFound().build();

            Subscription updated = subscriptionRepository.save(subscription);
            return ResponseEntity.ok(updated);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscription(@PathVariable String id) {
        try {
            Optional<Subscription> existing = subscriptionRepository.findById(id);
            if (existing.isEmpty()) return ResponseEntity.notFound().build();

            subscriptionRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
