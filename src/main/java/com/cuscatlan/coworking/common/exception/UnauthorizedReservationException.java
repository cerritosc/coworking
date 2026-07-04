package com.cuscatlan.coworking.common.exception;

public class UnauthorizedReservationException extends BusinessException {

    public UnauthorizedReservationException() {

        super(
                ErrorCode.ACCESS_DENIED,
                "You are not allowed to access this reservation."
        );

    }

}