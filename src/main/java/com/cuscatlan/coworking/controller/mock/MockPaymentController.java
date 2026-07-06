package com.cuscatlan.coworking.controller.mock;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cuscatlan.coworking.dto.request.payment.PaymentRequest;
import com.cuscatlan.coworking.integration.payment.dto.PaymentValidationResponse;

@RestController
@RequestMapping("/mock/payment")
public class MockPaymentController {

	@PostMapping("/validate")
    public PaymentValidationResponse validate(

            @RequestBody PaymentRequest request,

            @RequestParam(defaultValue = "0")
            long delay,

            @RequestParam(defaultValue = "true")
            boolean approved,

            @RequestParam(defaultValue = "200")
            int status)

            throws InterruptedException {

        System.out.println(">>> Mock Payment API invoked");

        Thread.sleep(delay);

        if (status == 500) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Gateway unavailable");
        }

        return new PaymentValidationResponse(
                approved,
                approved ? UUID.randomUUID().toString() : null,
                approved ? "Payment approved"
                         : "Payment rejected");
    }

}