package com.cuscatlan.coworking.service.reservation;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cuscatlan.coworking.common.exception.InvalidReservationDateException;
import com.cuscatlan.coworking.common.exception.InvalidReservationDurationException;
import com.cuscatlan.coworking.common.exception.OverlappingReservationException;
import com.cuscatlan.coworking.common.exception.SpaceInactiveException;
import com.cuscatlan.coworking.common.exception.UserDisabledException;
import com.cuscatlan.coworking.dto.request.reservation.CreateReservationRequest;
import com.cuscatlan.coworking.entity.Space;
import com.cuscatlan.coworking.entity.User;
import com.cuscatlan.coworking.repository.ReservationRepository;
import com.cuscatlan.coworking.support.TestDataFactory;

@ExtendWith(MockitoExtension.class)
class ReservationValidatorTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationValidator validator;

    private User user;

    private Space space;

    private CreateReservationRequest request;

    @BeforeEach
    void setUp() {

        user = TestDataFactory.validUser();

        space = TestDataFactory.validMeetingRoom();

        request = TestDataFactory.validReservationRequest();

    }

    @Test
    void shouldValidateReservationSuccessfully() {

        when(reservationRepository.existsOverlappingReservation(
                any(),
                any(),
                any()))
                .thenReturn(false);

        assertDoesNotThrow(() ->
                validator.validateCreation(
                        user,
                        space,
                        request));

        verify(reservationRepository, times(1))
                .existsOverlappingReservation(
                        eq(space.getId()),
                        eq(request.getStartDateTime()),
                        eq(request.getEndDateTime()));

    }

    @Test
    void shouldThrowWhenUserIsDisabled() {

        user.setEnabled(false);

        assertThrows(
                UserDisabledException.class,
                () -> validator.validateCreation(
                        user,
                        space,
                        request));

        verify(reservationRepository, never())
                .existsOverlappingReservation(
                        any(),
                        any(),
                        any());

    }

    @Test
    void shouldThrowWhenSpaceIsInactive() {

        space.setActive(false);

        assertThrows(
                SpaceInactiveException.class,
                () -> validator.validateCreation(
                        user,
                        space,
                        request));

        verify(reservationRepository, never())
                .existsOverlappingReservation(
                        any(),
                        any(),
                        any());

    }

    @Test
    void shouldThrowWhenEndDateIsBeforeStartDate() {

        request.setEndDateTime(
                request.getStartDateTime().minusMinutes(10));

        assertThrows(
                InvalidReservationDateException.class,
                () -> validator.validateCreation(
                        user,
                        space,
                        request));

    }

    @Test
    void shouldThrowWhenReservationIsInPast() {

        request.setStartDateTime(
                LocalDateTime.now().minusHours(2));

        request.setEndDateTime(
                LocalDateTime.now().minusHours(1));

        assertThrows(
                InvalidReservationDateException.class,
                () -> validator.validateCreation(
                        user,
                        space,
                        request));

    }

    @Test
    void shouldThrowWhenDurationIsLessThanOneHour() {

        request.setEndDateTime(
                request.getStartDateTime().plusMinutes(30));

        assertThrows(
                InvalidReservationDurationException.class,
                () -> validator.validateCreation(
                        user,
                        space,
                        request));

    }

    @Test
    void shouldThrowWhenReservationOverlaps() {

        when(reservationRepository.existsOverlappingReservation(
                any(),
                any(),
                any()))
                .thenReturn(true);

        assertThrows(
                OverlappingReservationException.class,
                () -> validator.validateCreation(
                        user,
                        space,
                        request));

    }

}