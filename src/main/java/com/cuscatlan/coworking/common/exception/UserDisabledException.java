package com.cuscatlan.coworking.common.exception;

public class UserDisabledException extends BusinessException {

    public UserDisabledException() {
        super(ErrorCode.USER_DISABLED);
    }

    public UserDisabledException(Long userId) {
        super(
                ErrorCode.USER_DISABLED,
                "User with id %d is disabled.".formatted(userId)
        );
    }

}