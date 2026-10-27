package com.example.user_management.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.user_management.data.Role;
import com.example.user_management.data.User;
import com.example.user_management.utils.JwtUtil;

import java.time.Duration;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {    

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
    
    @GetMapping(produces = "text/event-stream")
    public Flux<Map<String, String>> getStreamOfUsers(
        @RequestHeader(value = "Authorization", required = true) String authorization,
        @RequestParam(required = true) String role) {

        if (!authorization.startsWith("Bearer ")) {
            return Flux.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization header must start with 'Bearer '"));
        }    
        
        var token = authorization.substring(7); // Remove "Bearer " prefix
        String decodedRole;
        try {
            var decodedRoleClaim = JwtUtil.decodeRole(token);
            if (decodedRoleClaim.isNull()) {
                return Flux.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid token provided."));
            }
            decodedRole = decodedRoleClaim.asString();
            var roleEnum = Role.fromString(role);
            if (!User.allowedFilter(decodedRole, roleEnum)) {
                return Flux.error(new ResponseStatusException(HttpStatus.FORBIDDEN, "Role not allowed to access this resource."));
            }
        } catch (Exception e) {
            return Flux.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid token provided."));
        }                
        
        // flus just from a list of users filtered by role, if provided. If role is null or "all", return all users.
        final Set<Role> allowedRoles;
        try {
            var roleEnum = Role.fromString(role);
            allowedRoles = Set.of(roleEnum);
        } catch (IllegalArgumentException e) {
            return Flux.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid role provided."));
        }

        return Flux.fromStream(
            // Sample users
            Arrays.stream(User.USERS).filter(user -> allowedRoles.contains(user.role())).map(User::toMap)
        )
          .delayElements(Duration.ofSeconds(1))
          .doOnCancel(() -> System.out.println("Stream cancelled by UI")); // Delays each item by 1 second
    }
}