package dev.hamasakis.etl.dtos;

import dev.hamasakis.etl.enums.Role;

public record UpdateUserRequest(String name, String email, Role role, Boolean active) {
}
