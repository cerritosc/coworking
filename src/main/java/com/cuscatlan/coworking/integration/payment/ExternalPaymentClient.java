package com.cuscatlan.coworking.integration.payment;

import com.cuscatlan.coworking.dto.request.payment.PaymentRequest;

public interface ExternalPaymentClient {

    boolean validate(PaymentRequest request);

}