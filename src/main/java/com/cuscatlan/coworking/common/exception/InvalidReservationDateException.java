package com.cuscatlan.coworking.common.exception;

public class InvalidReservationDateException extends BusinessException {

    public InvalidReservationDateException() {
        super(ErrorCode.INVALID_RESERVATION_DATE);
    }

    public InvalidReservationDateException(String message) {
        super(ErrorCode.INVALID_RESERVATION_DATE, message);
    }

}