package com.cuscatlan.coworking.service.auth;

import org.springframework.stereotype.Service;

import com.cuscatlan.coworking.common.exception.UserNotFoundException;
import com.cuscatlan.coworking.entity.User;
import com.cuscatlan.coworking.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl
        implements AuthenticationService {

    private final UserRepository userRepository;

    @Override
    public User getCurrentUser() {

        return userRepository.findById(1L)
                .orElseThrow(() ->
                        new UserNotFoundException(1L));

    }

}