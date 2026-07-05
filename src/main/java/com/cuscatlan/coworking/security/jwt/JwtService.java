package com.cuscatlan.coworking.security.jwt;

import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;

public interface JwtService {

    String generateToken(UserDetails userDetails);

    String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails);

    String extractUsername(String token);

    <T> T extractClaim(
            String token,
            Function<Claims, T> claimsResolver);

    boolean isTokenValid(
            String token,
            UserDetails userDetails);

}