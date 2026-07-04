package com.cuscatlan.coworking.enums;

public enum ReservationStatus {

    PENDING_PAYMENT,
    CONFIRMED,
    CANCELLED,
    COMPLETED;

    public boolean isFinalStatus() {
        return this == CANCELLED || this == COMPLETED;
    }

    public boolean canBeCancelled() {
        return this == PENDING_PAYMENT || this == CONFIRMED;
    }

}