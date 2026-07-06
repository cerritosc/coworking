package com.cuscatlan.coworking.service.impl;

import org.springframework.stereotype.Service;

import com.cuscatlan.coworking.dto.request.payment.PaymentRequest;
import com.cuscatlan.coworking.integration.payment.ExternalPaymentClient;
import com.cuscatlan.coworking.service.PaymentService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final ExternalPaymentClient externalPaymentClient;

    @Override
    @CircuitBreaker(
            name = "payment-service",
            fallbackMethod = "paymentFallback")
    public boolean validatePayment(PaymentRequest request) {

        return externalPaymentClient.validate(request);

    }

    public boolean paymentFallback(
            PaymentRequest request,
            Exception ex) {

        System.out.println(
                ">>> Circuit Breaker activated: "
                        + ex.getClass().getSimpleName());

        return false;

    }

}