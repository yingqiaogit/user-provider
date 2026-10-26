package com.example.user_management.data;

import java.util.Map;
import java.util.Set;

public enum Role {
        MANAGER("manager"),
        ADMIN("admin"),
        USER("user");

        private final String roleName;

        static final Map<String, Set<Role>> ROLE_ALLOWED_FILTER = Map.of(
        Role.MANAGER.getRoleName(), Set.of(Role.MANAGER, Role.USER),
        Role.ADMIN.getRoleName(), Set.of(Role.ADMIN, Role.MANAGER, Role.USER),
        Role.USER.getRoleName(), Set.of(Role.USER)
        );

        static final Map<String, Role> ROLE_NAME_MAP = Map.of(
            Role.MANAGER.getRoleName(), Role.MANAGER,
            Role.ADMIN.getRoleName(), Role.ADMIN,
            Role.USER.getRoleName(), Role.USER
        );

        public static Role fromString(String roleName) {
            var role = ROLE_NAME_MAP.get(roleName);
            if (role == null) {
                throw new IllegalArgumentException("No enum constant with role name: " + roleName);
            }
            return role;
        }

        private Role(String roleName) {
            this.roleName = roleName;
        }

        public String getRoleName() {
            return roleName;
        }
    }