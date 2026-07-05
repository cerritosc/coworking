package com.cuscatlan.coworking.service;

import com.cuscatlan.coworking.dto.request.auth.AuthenticationRequest;
import com.cuscatlan.coworking.dto.request.auth.RegisterRequest;
import com.cuscatlan.coworking.dto.response.auth.AuthenticationResponse;
import com.cuscatlan.coworking.dto.response.user.UserResponse;

public interface AuthenticationService {

    AuthenticationResponse authenticate(
            AuthenticationRequest request);
    
    UserResponse register(
            RegisterRequest request);

}