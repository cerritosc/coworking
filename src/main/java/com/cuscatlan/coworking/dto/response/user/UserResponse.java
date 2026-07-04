package com.cuscatlan.coworking.dto.response.user;

import com.cuscatlan.coworking.enums.Role;

public record UserResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        Role role
) {
}