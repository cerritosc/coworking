package com.cuscatlan.coworking.service.impl;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.cuscatlan.coworking.entity.Reservation;
import com.cuscatlan.coworking.service.EmailNotificationService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailNotificationServiceImpl
        implements EmailNotificationService {

    @Async
    @Override
    public void sendReservationConfirmation(
            Reservation reservation) {

        try {

            Thread.sleep(5000);

            log.info("""
                    
                    =====================================
                    Reservation confirmation email sent
                    User: {}
                    Email: {}
                    Reservation: {}
                    =====================================
                    
                    """,
                    reservation.getUser().getFirstName(),
                    reservation.getUser().getEmail(),
                    reservation.getId());

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

        }

    }

}