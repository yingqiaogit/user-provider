package com.example.user_management.data;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

public record User(int id, String name, Role role, String email, LocalDateTime lastLoginTime) {
        // This is a simple record class to represent a user    
    private static User createUser(int id, String name, Role role, String email) {
        return new User(id, name, role, email, LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
    }

    public static final User[] USERS = {
        createUser(0, "John Doe", Role.MANAGER, "jo************"),
        createUser(1, "Jane Smith", Role.ADMIN, "ja**************"),
        createUser(2, "Alice Johnson", Role.MANAGER, "al**************"),
        createUser(3, "Bob Brown", Role.USER, "bo**************")
    };    
    
    public static boolean allowedFilter(String role, Role filterRole) {
        if (role == null || filterRole == null) {
            return false;
        }        
        return Role.ROLE_ALLOWED_FILTER.get(role).contains(filterRole);
    }    

    public static Map<String, String> toMap(User user) {
        return Map.of(
            "id", String.valueOf(user.id()),
            "name", user.name(),
            "role", user.role().getRoleName(),
            "email", user.email(),
            "lastLoginTime", user.lastLoginTime().toString()
        );
    }
}