package com.example.user_management.api;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.user_management.data.Role;
import com.example.user_management.utils.JwtUtil;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {

    @PostMapping(value = "/assertion")
    public Mono<Map<String, String>> postAssertion(@RequestBody(required = true) Map<String, String> request) {
        
        var role = request.get("role");
        if (role == null ) {
            return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));
        }

        try {
            Role.fromString(role);
        } catch (IllegalArgumentException e) {
            return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid role provided."));
        }
        
        var token = JwtUtil.generateToken(role);

        return Mono.just(Map.of(
                "token", token
        ));
    }
}
