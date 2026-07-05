package com.cuscatlan.coworking.dto.response.auth;

import com.cuscatlan.coworking.enums.Role;

public record AuthenticationResponse(

        String token,

        Long userId,

        String fullName,

        String email,

        Role role

) {
}