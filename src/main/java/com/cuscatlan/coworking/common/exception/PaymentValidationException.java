package com.cuscatlan.coworking.common.exception;

public class PaymentValidationException extends BusinessException {

    public PaymentValidationException() {

        super(ErrorCode.PAYMENT_VALIDATION_FAILED);

    }

}