package dev.hamasakis.etl.dtos;

import dev.hamasakis.etl.enums.Permission;
import dev.hamasakis.etl.enums.Role;
import dev.hamasakis.etl.models.User;

import java.util.List;

public record UserResponse(String email, String name, Role role, boolean active, List<Permission> permissions) {
    public static UserResponse fromModel(User user) {
        return new UserResponse(
                user.getEmail(),
                user.getName(),
                user.getRole(),
                user.isActive(),
                user.getRole().getPermissions().stream().toList()
        );
    }
}
