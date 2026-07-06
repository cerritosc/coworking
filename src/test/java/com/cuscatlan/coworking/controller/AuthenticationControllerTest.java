package com.cuscatlan.coworking.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.cuscatlan.coworking.common.util.ApiPaths;
import com.cuscatlan.coworking.config.TestSecurityConfig;
import com.cuscatlan.coworking.dto.request.auth.AuthenticationRequest;
import com.cuscatlan.coworking.dto.request.auth.RegisterRequest;
import com.cuscatlan.coworking.dto.response.auth.AuthenticationResponse;
import com.cuscatlan.coworking.dto.response.user.UserResponse;
import com.cuscatlan.coworking.enums.Role;
import com.cuscatlan.coworking.service.AuthenticationService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationService authenticationService;

    @Test
    @DisplayName("Should authenticate successfully")
    void shouldAuthenticateSuccessfully() throws Exception {

        AuthenticationRequest request =
                new AuthenticationRequest(
                        "carlos@test.com",
                        "Password123");

        AuthenticationResponse response =
                new AuthenticationResponse(
                        "jwt-token",
                        1L,
                        "Carlos Cerritos",
                        "carlos@test.com",
                        Role.USER);

        when(authenticationService.authenticate(any()))
                .thenReturn(response);

        mockMvc.perform(post(ApiPaths.AUTH + "/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))

                .andExpect(status().isOk())

                .andExpect(jsonPath("$.success").value(true))

                .andExpect(jsonPath("$.message")
                        .value("Authentication successful."))

                .andExpect(jsonPath("$.data.token")
                        .value("jwt-token"))

                .andExpect(jsonPath("$.data.email")
                        .value("carlos@test.com"));

    }

    @Test
    @DisplayName("Should register successfully")
    void shouldRegisterSuccessfully() throws Exception {

        RegisterRequest request = new RegisterRequest();

        request.setFirstName("Carlos");
        request.setLastName("Cerritos");
        request.setEmail("carlos@test.com");
        request.setPassword("Password123");

        UserResponse response =
                new UserResponse(
                        1L,
                        "Carlos",
                        "Cerritos",
                        "carlos@test.com",
                        Role.USER);

        when(authenticationService.register(any()))
                .thenReturn(response);

        mockMvc.perform(post(ApiPaths.AUTH + "/register")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))

                .andExpect(status().isCreated())

                .andExpect(jsonPath("$.success").value(true))

                .andExpect(jsonPath("$.message")
                        .value("User registered successfully."))

                .andExpect(jsonPath("$.data.firstName")
                        .value("Carlos"))

                .andExpect(jsonPath("$.data.email")
                        .value("carlos@test.com"));

    }

    @Test
    @DisplayName("Should return bad request when register request is invalid")
    void shouldReturnBadRequestWhenRegisterRequestIsInvalid()
            throws Exception {

        RegisterRequest request = new RegisterRequest();

        mockMvc.perform(post(ApiPaths.AUTH + "/register")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))

                .andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("Should return bad request when login request is invalid")
    void shouldReturnBadRequestWhenLoginRequestIsInvalid()
            throws Exception {

        AuthenticationRequest request =
                new AuthenticationRequest(
                        "",
                        "");

        mockMvc.perform(post(ApiPaths.AUTH + "/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))

                .andExpect(status().isBadRequest());

    }

}