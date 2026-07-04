package com.cuscatlan.coworking.service;

import com.cuscatlan.coworking.dto.request.payment.PaymentRequest;

public interface PaymentService {

	boolean validatePayment(
            PaymentRequest paymentRequest
    );

}