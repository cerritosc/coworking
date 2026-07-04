package com.cuscatlan.coworking.service.reservation;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import com.cuscatlan.coworking.common.exception.InvalidReservationDateException;
import com.cuscatlan.coworking.common.exception.InvalidReservationDurationException;
import com.cuscatlan.coworking.common.exception.OverlappingReservationException;
import com.cuscatlan.coworking.common.exception.SpaceInactiveException;
import com.cuscatlan.coworking.common.exception.SpaceNotFoundException;
import com.cuscatlan.coworking.common.exception.UserDisabledException;
import com.cuscatlan.coworking.entity.Space;
import com.cuscatlan.coworking.entity.User;
import com.cuscatlan.coworking.dto.request.reservation.CreateReservationRequest;
import com.cuscatlan.coworking.repository.ReservationRepository;

@Component
@RequiredArgsConstructor
public class ReservationValidator {

    private static final long MINIMUM_RESERVATION_MINUTES = 60;

    private final ReservationRepository reservationRepository;

    public void validateCreation(
            User user,
            Space space,
            CreateReservationRequest request) {

        validateUser(user);
        validateSpace(space);
        validateDates(
                request.getStartDateTime(),
                request.getEndDateTime());

        validateReservationDuration(
                request.getStartDateTime(),
                request.getEndDateTime());

        validateReservationOverlap(
                space.getId(),
                request.getStartDateTime(),
                request.getEndDateTime());

    }

    private void validateUser(User user) {
    	
    	Objects.requireNonNull(user, "User must not be null");

        if (!Boolean.TRUE.equals(user.getEnabled())) {
            throw new UserDisabledException(user.getId());
        }

    }
    
    private void validateSpace(Space space) {

        Objects.requireNonNull(space, "Space must not be null");

        if (!Boolean.TRUE.equals(space.getActive())) {
            throw new SpaceInactiveException(space.getId());
        }

    }

    private void validateDates(
            LocalDateTime start,
            LocalDateTime end) {
    	
    	Objects.requireNonNull(start, "Start date is required.");

    	Objects.requireNonNull(end, "End date is required.");

        if (start == null || end == null) {
            throw new InvalidReservationDateException(
                    "Reservation dates are required."
            );
        }

        if (!end.isAfter(start)) {
            throw new InvalidReservationDateException(
                    "End date must be after start date."
            );
        }

        if (start.isBefore(LocalDateTime.now())) {
            throw new InvalidReservationDateException(
                    "Reservations cannot be created in the past."
            );
        }

    }

    private void validateReservationDuration(
            LocalDateTime start,
            LocalDateTime end) {

        long minutes = Duration.between(start, end).toMinutes();

        if (minutes < MINIMUM_RESERVATION_MINUTES) {
            throw new InvalidReservationDurationException(
                    "Minimum reservation duration is one hour."
            );
        }

    }

    private void validateReservationOverlap(
            Long spaceId,
            LocalDateTime start,
            LocalDateTime end) {

        boolean exists = reservationRepository
                .existsOverlappingReservation(
                        spaceId,
                        start,
                        end);

        if (exists) {
            throw new OverlappingReservationException();
        }

    }

}