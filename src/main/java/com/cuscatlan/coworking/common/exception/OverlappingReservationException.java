package com.cuscatlan.coworking.common.exception;

public class OverlappingReservationException extends BusinessException {

    public OverlappingReservationException() {

        super(ErrorCode.RESERVATION_OVERLAP);

    }

}