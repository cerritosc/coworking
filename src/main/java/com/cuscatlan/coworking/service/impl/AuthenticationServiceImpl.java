package com.cuscatlan.coworking.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cuscatlan.coworking.entity.User;
import com.cuscatlan.coworking.enums.Role;
import com.cuscatlan.coworking.mapper.UserMapper;
import com.cuscatlan.coworking.common.exception.UserAlreadyExistsException;
import com.cuscatlan.coworking.dto.request.auth.AuthenticationRequest;
import com.cuscatlan.coworking.dto.request.auth.RegisterRequest;
import com.cuscatlan.coworking.dto.response.auth.AuthenticationResponse;
import com.cuscatlan.coworking.dto.response.user.UserResponse;
import com.cuscatlan.coworking.repository.UserRepository;
import com.cuscatlan.coworking.security.jwt.JwtService;
import com.cuscatlan.coworking.security.user.CustomUserDetails;
import com.cuscatlan.coworking.service.AuthenticationService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
	
	private final UserMapper userMapper;
	
	private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final JwtService jwtService;

    @Override
    public AuthenticationResponse authenticate(
            AuthenticationRequest request) {

        authenticationManager.authenticate(

                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password())

        );

        User user = userRepository
                .findByEmail(request.email())
                .orElseThrow(() ->
                new UsernameNotFoundException(
                        "User not found: " + request.email()));

        CustomUserDetails userDetails =
                new CustomUserDetails(user);

        String token =
                jwtService.generateToken(userDetails);

        return new AuthenticationResponse(

                token,

                user.getId(),

                user.getFirstName()
                        + " "
                        + user.getLastName(),

                user.getEmail(),

                user.getRole()

        );

    }
    
    @Override
    @Transactional
    public UserResponse register(RegisterRequest request) {

    	if (userRepository.existsByEmail(request.getEmail())) {
    	    throw new UserAlreadyExistsException(request.getEmail());
    	}

        User user = userMapper.toEntity(request);

        user.setPassword(
                passwordEncoder.encode(request.getPassword()));

        user.setRole(Role.USER);

        user.setEnabled(Boolean.TRUE);

        userRepository.save(user);

        return userMapper.toResponse(user);

    }

}