package com.cuscatlan.coworking.service.impl;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import com.cuscatlan.coworking.dto.request.payment.PaymentRequest;
import com.cuscatlan.coworking.service.PaymentService;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {

    @Override
    public boolean validatePayment(PaymentRequest paymentRequest) {

        log.info(
                "Validating payment for reservation {}, amount {} {}",
                paymentRequest.getSpaceId(),
                paymentRequest.getAmount(),
                paymentRequest.getCurrency()
        );

        /*
         * Simulación del proveedor de pagos.
         */
        return true;

    }

}