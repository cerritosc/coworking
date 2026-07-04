package com.cuscatlan.coworking.common.exception;

public class InvalidReservationDurationException extends BusinessException {

    public InvalidReservationDurationException() {
        super(ErrorCode.INVALID_RESERVATION_DURATION);
    }

    public InvalidReservationDurationException(String message) {
        super(ErrorCode.INVALID_RESERVATION_DURATION, message);
    }

}