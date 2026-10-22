package com.example.user_management.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import java.util.Arrays;
import java.util.Map;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1")
public class ReactivePracticeController {
    private static final User[] USERS = {
            new User(0, "John Doe", "user", "john.doe@example.com", LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)),
            new User(1, "Jane Smith", "admin", "jane.smith@example.com", LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)),
            new User(2, "Alice Johnson", "user", "alice.johnson@example.com", LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)),
            new User(3, "Bob Brown", "viewer", "bob.brown@example.com", LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
        };

    // 1. Single Item (Mono)
    @GetMapping("/one")
    public Mono<Map<String, String>> getSingleGreeting() {
        return Mono.just(Map.of(
                "message", "Hello, this is a single greeting from the reactive controller!"
        ));
    }

    // 2. Multiple Items (Flux) streamed as a Server-Sent Event (SSE)
    @GetMapping(value = "/items", produces = "text/event-stream")
    public Flux<String> getStreamOfData() {
        return Flux.just("Item 1", "Item 2", "Item 3", "Item 4", "Item 5")
                   .delayElements(Duration.ofSeconds(1))
                   .doOnCancel(() -> System.out.println("Stream cancelled by UI")); // Delays each item by 1 second
    }

    @GetMapping(value = "/users", produces = "text/event-stream")
    public Flux<User> getStreamOfUsers(@RequestParam(required = false) String role) {
        
        // flus just from a list of users filtered by role, if provided. If role is null or "all", return all users.

        return Flux.fromStream(
            // Sample users
            Arrays.stream(USERS).filter(user -> role == null || user.role().equals(role) || role.equals("all")) 
        )
          .delayElements(Duration.ofSeconds(1))
          .doOnCancel(() -> System.out.println("Stream cancelled by UI")); // Delays each item by 1 second
    }

    private record User(int id, String name, String role, String email, LocalDateTime lastLoginTime) {
        // This is a simple record class to represent a user    
    }
}