package com.cuscatlan.coworking.integration.payment.dto;

public record PaymentValidationResponse(

        boolean approved,

        String transactionId,

        String message

) {
}