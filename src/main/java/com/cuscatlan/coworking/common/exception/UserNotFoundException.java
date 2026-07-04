package com.cuscatlan.coworking.common.exception;

public class UserNotFoundException extends BusinessException {

    public UserNotFoundException(Long id) {

        super(
                ErrorCode.USER_NOT_FOUND,
                "User with id %d was not found.".formatted(id)
        );

    }

    public UserNotFoundException(String email) {

        super(
                ErrorCode.USER_NOT_FOUND,
                "User with email %s was not found.".formatted(email)
        );

    }

}