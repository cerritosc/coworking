package com.cuscatlan.coworking.service;

import java.util.List;

import com.cuscatlan.coworking.dto.request.auth.RegisterRequest;
import com.cuscatlan.coworking.dto.response.user.UserResponse;

public interface UserService {

    UserResponse create(RegisterRequest request);

    UserResponse findById(Long id);

    List<UserResponse> findAll();

}