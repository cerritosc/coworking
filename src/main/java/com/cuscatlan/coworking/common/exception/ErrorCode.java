package com.cuscatlan.coworking.common.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    VALIDATION_ERROR(
            HttpStatus.BAD_REQUEST,
            "Validation failed."
    ),

    USER_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "User not found."
    ),

    SPACE_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "Space not found."
    ),

    RESERVATION_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "Reservation not found."
    ),

    RESERVATION_OVERLAP(
            HttpStatus.CONFLICT,
            "The selected time slot is already reserved."
    ),

    PAYMENT_VALIDATION_FAILED(
            HttpStatus.BAD_GATEWAY,
            "Payment validation service is unavailable."
    ),

    ACCESS_DENIED(
            HttpStatus.FORBIDDEN,
            "Access denied."
    ),

    UNAUTHORIZED(
            HttpStatus.UNAUTHORIZED,
            "Authentication required."
    ),

    INTERNAL_SERVER_ERROR(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Unexpected server error."
    ),
    
    INVALID_RESERVATION_DATE(
            HttpStatus.BAD_REQUEST,
            "Invalid reservation dates."
    ),

    SPACE_INACTIVE(
            HttpStatus.CONFLICT,
            "Space is inactive."
    ),

    USER_DISABLED(
            HttpStatus.FORBIDDEN,
            "User account is disabled."
    ),

    INVALID_RESERVATION_DURATION(
            HttpStatus.BAD_REQUEST,
            "Invalid reservation duration."
    ),

	UNSUPPORTED_SPACE_TYPE(
	        HttpStatus.INTERNAL_SERVER_ERROR,
	        "No pricing strategy found for the selected space type."
	),
	
	RESERVATION_CANNOT_BE_CANCELLED(
	        HttpStatus.CONFLICT,
	        "Reservation cannot be cancelled."
	);
	

    private final HttpStatus httpStatus;
    private final String defaultMessage;

    ErrorCode(HttpStatus httpStatus, String defaultMessage) {
        this.httpStatus = httpStatus;
        this.defaultMessage = defaultMessage;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

}