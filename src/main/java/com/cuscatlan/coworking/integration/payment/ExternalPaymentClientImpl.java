package com.cuscatlan.coworking.integration.payment;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.cuscatlan.coworking.dto.request.payment.PaymentRequest;
import com.cuscatlan.coworking.integration.payment.dto.PaymentValidationResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ExternalPaymentClientImpl
        implements ExternalPaymentClient {

    private final RestClient paymentRestClient;

    @Override
    public boolean validate(PaymentRequest request) {

        PaymentValidationResponse response =
                paymentRestClient
                        .post()
                        .uri("/payment/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(request)
                        .retrieve()
                        .body(PaymentValidationResponse.class);

        return response != null
                && response.approved();

    }

}