package com.cuscatlan.coworking.common.exception;

public class UserAlreadyExistsException extends BusinessException {

    public UserAlreadyExistsException(String email) {
        super(
                ErrorCode.USER_ALREADY_EXISTS,
                "User already exists with email: " + email);
    }

}