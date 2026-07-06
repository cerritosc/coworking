package com.cuscatlan.coworking.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.cuscatlan.coworking.common.exception.UserAlreadyExistsException;
import com.cuscatlan.coworking.dto.request.auth.AuthenticationRequest;
import com.cuscatlan.coworking.dto.request.auth.RegisterRequest;
import com.cuscatlan.coworking.dto.response.auth.AuthenticationResponse;
import com.cuscatlan.coworking.dto.response.user.UserResponse;
import com.cuscatlan.coworking.entity.User;
import com.cuscatlan.coworking.enums.Role;
import com.cuscatlan.coworking.mapper.UserMapper;
import com.cuscatlan.coworking.repository.UserRepository;
import com.cuscatlan.coworking.security.jwt.JwtService;
import com.cuscatlan.coworking.support.TestDataFactory;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthenticationServiceImpl service;

    private User user;

    @BeforeEach
    void setUp() {

        user = TestDataFactory.validUser();

    }

    @Test
    @DisplayName("Should register user successfully")
    void shouldRegisterUserSuccessfully() {

    	RegisterRequest request = TestDataFactory.validRegisterRequest();

    	UserResponse response =
    	        TestDataFactory.validUserResponse();

        when(userRepository.existsByEmail(request.getEmail()))
                .thenReturn(false);

        when(userMapper.toEntity(request))
                .thenReturn(user);

        when(passwordEncoder.encode(request.getPassword()))
                .thenReturn("encoded-password");

        when(userMapper.toResponse(any(User.class)))
                .thenReturn(response);

        UserResponse result =
                service.register(request);

        assertNotNull(result);

        assertEquals(
                "Carlos",
                result.firstName());

        verify(userRepository)
                .save(any(User.class));

        ArgumentCaptor<User> captor =
                ArgumentCaptor.forClass(User.class);

        verify(userRepository)
                .save(captor.capture());

        User saved = captor.getValue();

        assertEquals(
                Role.USER,
                saved.getRole());

        assertTrue(saved.getEnabled());

        assertEquals(
                "encoded-password",
                saved.getPassword());

    }

    @Test
    @DisplayName("Should throw exception when email already exists")
    void shouldThrowWhenEmailAlreadyExists() {

        RegisterRequest request = new RegisterRequest();
        request.setEmail("carlos@test.com");

        when(userRepository.existsByEmail(request.getEmail()))
                .thenReturn(true);

        assertThrows(
                UserAlreadyExistsException.class,
                () -> service.register(request));

        verify(userRepository, never())
                .save(any());

    }

    @Test
    @DisplayName("Should authenticate successfully")
    void shouldAuthenticateSuccessfully() {

        AuthenticationRequest request =
                new AuthenticationRequest(
                        "carlos@test.com",
                        "Password123");

        when(userRepository.findByEmail(request.email()))
                .thenReturn(Optional.of(user));

        when(jwtService.generateToken(any()))
                .thenReturn("jwt-token");

        AuthenticationResponse response =
                service.authenticate(request);

        assertNotNull(response);

        assertEquals(
                "jwt-token",
                response.token());

        assertEquals(
                user.getEmail(),
                response.email());

        verify(authenticationManager)
                .authenticate(any());

        verify(jwtService)
                .generateToken(any());

    }

}