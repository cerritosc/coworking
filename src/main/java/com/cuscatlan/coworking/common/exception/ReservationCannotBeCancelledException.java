package com.cuscatlan.coworking.common.exception;

public class ReservationCannotBeCancelledException
extends BusinessException {

public ReservationCannotBeCancelledException(Long reservationId) {

super(
        ErrorCode.RESERVATION_CANNOT_BE_CANCELLED,
        "Reservation %d cannot be cancelled."
                .formatted(reservationId));

}

}