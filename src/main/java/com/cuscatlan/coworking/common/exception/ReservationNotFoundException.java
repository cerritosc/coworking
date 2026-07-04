package com.cuscatlan.coworking.common.exception;

public class ReservationNotFoundException extends BusinessException {

    public ReservationNotFoundException(Long id) {

        super(
                ErrorCode.RESERVATION_NOT_FOUND,
                "Reservation with id %d was not found.".formatted(id)
        );

    }

}