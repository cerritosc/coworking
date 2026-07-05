package com.cuscatlan.coworking.service.auth;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.cuscatlan.coworking.entity.User;
import com.cuscatlan.coworking.security.user.CustomUserDetails;

@Component
public class AuthenticationFacadeImpl
        implements AuthenticationFacade {

    @Override
    public User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {

            throw new IllegalStateException(
                    "No authenticated user found.");

        }

        CustomUserDetails principal =
                (CustomUserDetails) authentication.getPrincipal();

        return principal.getUser();

    }

}