package com.cuscatlan.coworking.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cuscatlan.coworking.common.exception.PaymentValidationException;
import com.cuscatlan.coworking.common.exception.ReservationCannotBeCancelledException;
import com.cuscatlan.coworking.dto.request.payment.PaymentRequest;
import com.cuscatlan.coworking.dto.request.reservation.CreateReservationRequest;
import com.cuscatlan.coworking.dto.response.reservation.ReservationResponse;
import com.cuscatlan.coworking.entity.Reservation;
import com.cuscatlan.coworking.entity.Space;
import com.cuscatlan.coworking.entity.User;
import com.cuscatlan.coworking.enums.ReservationStatus;
import com.cuscatlan.coworking.mapper.PaymentMapper;
import com.cuscatlan.coworking.mapper.ReservationMapper;
import com.cuscatlan.coworking.repository.ReservationRepository;
import com.cuscatlan.coworking.repository.SpaceRepository;
import com.cuscatlan.coworking.service.EmailNotificationService;
import com.cuscatlan.coworking.service.PaymentService;
import com.cuscatlan.coworking.service.auth.AuthenticationFacade;
import com.cuscatlan.coworking.service.pricing.PricingStrategy;
import com.cuscatlan.coworking.service.pricing.PricingStrategyFactory;
import com.cuscatlan.coworking.service.reservation.ReservationValidator;
import com.cuscatlan.coworking.support.TestDataFactory;

@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private SpaceRepository spaceRepository;

    @Mock
    private ReservationMapper reservationMapper;

    @Mock
    private PaymentMapper paymentMapper;

    @Mock
    private ReservationValidator reservationValidator;

    @Mock
    private PricingStrategyFactory pricingStrategyFactory;

    @Mock
    private PricingStrategy pricingStrategy;

    @Mock
    private PaymentService paymentService;

    @Mock
    private AuthenticationFacade authenticationFacade;
    
    @Mock
    private EmailNotificationService emailNotificationService;

    @InjectMocks
    private ReservationServiceImpl service;

    private User user;

    private Space space;

    private CreateReservationRequest request;

    private PaymentRequest paymentRequest;

    private ReservationResponse reservationResponse;

    @BeforeEach
    void setUp() {

        user = TestDataFactory.validUser();

        space = TestDataFactory.validMeetingRoom();

        request = TestDataFactory.validReservationRequest();

        paymentRequest = PaymentRequest.builder()
                .userId(user.getId())
                .spaceId(space.getId())
                .amount(BigDecimal.TEN)
                .currency("USD")
                .build();

        reservationResponse =
                new ReservationResponse(
                        1L,
                        user.getId(),
                        space.getId(),
                        "Carlos Cerritos",
                        "Meeting Room",
                        request.getStartDateTime(),
                        request.getEndDateTime(),
                        ReservationStatus.CONFIRMED,
                        BigDecimal.TEN);

    }
    
    @Test
    @DisplayName("Should create reservation successfully")
    void shouldCreateReservationSuccessfully() {

        when(authenticationFacade.getCurrentUser())
                .thenReturn(user);

        when(spaceRepository.findByIdForUpdate(space.getId()))
                .thenReturn(Optional.of(space));

        doNothing()
                .when(reservationValidator)
                .validateCreation(user, space, request);

        when(pricingStrategyFactory.getStrategy(space.getType()))
                .thenReturn(pricingStrategy);

        when(pricingStrategy.calculatePrice(
                any(),
                any(),
                any()))
                .thenReturn(BigDecimal.TEN);

        when(paymentMapper.toPaymentRequest(
                any(),
                any(),
                any()))
                .thenReturn(paymentRequest);

        when(paymentService.validatePayment(paymentRequest))
                .thenReturn(true);

        when(reservationMapper.toResponse(any()))
                .thenReturn(reservationResponse);

        ReservationResponse response =
                service.create(request);

        assertNotNull(response);

        assertEquals(
                ReservationStatus.CONFIRMED,
                response.status());

        ArgumentCaptor<Reservation> captor =
                ArgumentCaptor.forClass(Reservation.class);

        verify(reservationRepository)
                .save(captor.capture());

        Reservation saved =
                captor.getValue();

        assertEquals(
                ReservationStatus.CONFIRMED,
                saved.getStatus());

        assertEquals(
                BigDecimal.TEN,
                saved.getTotalPrice());

        InOrder inOrder =
                inOrder(
                        authenticationFacade,
                        reservationValidator,
                        pricingStrategyFactory,
                        paymentService,
                        reservationRepository);

        inOrder.verify(authenticationFacade)
                .getCurrentUser();

        inOrder.verify(reservationValidator)
                .validateCreation(
                        user,
                        space,
                        request);

        inOrder.verify(pricingStrategyFactory)
                .getStrategy(space.getType());

        inOrder.verify(paymentService)
                .validatePayment(paymentRequest);

        inOrder.verify(reservationRepository)
                .save(any());
        
        verify(emailNotificationService)
        .sendReservationConfirmation(any(Reservation.class));

    }
    
    @Test
    @DisplayName("Should throw exception when payment validation fails")
    void shouldThrowWhenPaymentValidationFails() {

        when(authenticationFacade.getCurrentUser())
                .thenReturn(user);

        when(spaceRepository.findByIdForUpdate(space.getId()))
                .thenReturn(Optional.of(space));

        when(pricingStrategyFactory.getStrategy(space.getType()))
                .thenReturn(pricingStrategy);

        when(pricingStrategy.calculatePrice(
                any(),
                any(),
                any()))
                .thenReturn(BigDecimal.TEN);

        when(paymentMapper.toPaymentRequest(
                any(),
                any(),
                any()))
                .thenReturn(paymentRequest);

        when(paymentService.validatePayment(paymentRequest))
                .thenReturn(false);

        assertThrows(
                PaymentValidationException.class,
                () -> service.create(request));

        verify(reservationRepository, never())
                .save(any());

    }
    
    @Test
    @DisplayName("Should find reservation by id")
    void shouldFindReservationById() {

        Reservation reservation = Reservation.builder()
                .id(1L)
                .user(user)
                .space(space)
                .status(ReservationStatus.CONFIRMED)
                .build();

        when(reservationRepository.findDetailedById(1L))
                .thenReturn(Optional.of(reservation));

        when(reservationMapper.toResponse(reservation))
                .thenReturn(reservationResponse);

        ReservationResponse response =
                service.findById(1L);

        assertNotNull(response);

        assertEquals(
                reservationResponse.id(),
                response.id());

        verify(reservationRepository)
                .findDetailedById(1L);

        verify(reservationMapper)
                .toResponse(reservation);

    }
    
    @Test
    @DisplayName("Should return all reservations")
    void shouldReturnAllReservations() {

        Reservation reservation = Reservation.builder()
                .id(1L)
                .user(user)
                .space(space)
                .status(ReservationStatus.CONFIRMED)
                .build();

        when(reservationRepository.findAll())
                .thenReturn(List.of(reservation));

        when(reservationMapper.toResponse(reservation))
                .thenReturn(reservationResponse);

        List<ReservationResponse> response =
                service.findAll();

        assertEquals(
                1,
                response.size());

        verify(reservationRepository)
                .findAll();

        verify(reservationMapper)
                .toResponse(reservation);

    }
    
    @Test
    @DisplayName("Should return current user reservations")
    void shouldReturnCurrentUserReservations() {

        Reservation reservation = Reservation.builder()
                .id(1L)
                .user(user)
                .space(space)
                .status(ReservationStatus.CONFIRMED)
                .build();

        when(authenticationFacade.getCurrentUser())
                .thenReturn(user);

        when(reservationRepository.findByUserId(user.getId()))
                .thenReturn(List.of(reservation));

        when(reservationMapper.toResponse(reservation))
                .thenReturn(reservationResponse);

        List<ReservationResponse> response =
                service.findByCurrentUser();

        assertEquals(
                1,
                response.size());

        verify(authenticationFacade)
                .getCurrentUser();

        verify(reservationRepository)
                .findByUserId(user.getId());

        verify(reservationMapper)
                .toResponse(reservation);

    }
    
    @Test
    @DisplayName("Should cancel reservation successfully")
    void shouldCancelReservationSuccessfully() {

        Reservation reservation = Reservation.builder()
                .id(1L)
                .status(ReservationStatus.CONFIRMED)
                .build();

        when(reservationRepository.findById(1L))
                .thenReturn(Optional.of(reservation));

        service.cancel(1L);

        assertEquals(
                ReservationStatus.CANCELLED,
                reservation.getStatus());

        verify(reservationRepository)
                .findById(1L);

    }
    
    @Test
    @DisplayName("Should throw exception when reservation cannot be cancelled")
    void shouldThrowWhenReservationCannotBeCancelled() {

        Reservation reservation = Reservation.builder()
                .id(1L)
                .status(ReservationStatus.COMPLETED)
                .build();

        when(reservationRepository.findById(1L))
                .thenReturn(Optional.of(reservation));

        assertThrows(
                ReservationCannotBeCancelledException.class,
                () -> service.cancel(1L));

        verify(reservationRepository)
                .findById(1L);

    }

}