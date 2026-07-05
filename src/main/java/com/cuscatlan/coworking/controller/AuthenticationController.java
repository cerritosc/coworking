package com.cuscatlan.coworking.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cuscatlan.coworking.common.util.*;
import com.cuscatlan.coworking.common.response.ApiResponse;
import com.cuscatlan.coworking.dto.request.auth.AuthenticationRequest;
import com.cuscatlan.coworking.dto.request.auth.RegisterRequest;
import com.cuscatlan.coworking.dto.response.auth.AuthenticationResponse;
import com.cuscatlan.coworking.dto.response.user.UserResponse;
import com.cuscatlan.coworking.service.AuthenticationService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPaths.AUTH)
@Tag(name = "Authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> login(

            @Valid
            @RequestBody
            AuthenticationRequest request) {

        AuthenticationResponse response =
                authenticationService.authenticate(request);

        return ResponseEntity.ok(

                ApiResponse.success(

                        "Authentication successful.",

                        response)

        );

    }
    
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(

            @Valid
            @RequestBody
            RegisterRequest request) {

        UserResponse response =
                authenticationService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(
                                "User registered successfully.",
                                response));

    }

}