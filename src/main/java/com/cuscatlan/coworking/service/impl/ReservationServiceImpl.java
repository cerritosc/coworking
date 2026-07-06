package com.cuscatlan.coworking.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import com.cuscatlan.coworking.common.exception.PaymentValidationException;
import com.cuscatlan.coworking.common.exception.ReservationCannotBeCancelledException;
import com.cuscatlan.coworking.common.exception.ReservationNotFoundException;
import com.cuscatlan.coworking.common.exception.SpaceNotFoundException;
import com.cuscatlan.coworking.entity.Reservation;
import com.cuscatlan.coworking.entity.Space;
import com.cuscatlan.coworking.entity.User;
import com.cuscatlan.coworking.enums.ReservationStatus;
import com.cuscatlan.coworking.dto.request.payment.PaymentRequest;
import com.cuscatlan.coworking.dto.request.reservation.CreateReservationRequest;
import com.cuscatlan.coworking.dto.response.reservation.ReservationResponse;
import com.cuscatlan.coworking.mapper.PaymentMapper;
import com.cuscatlan.coworking.mapper.ReservationMapper;
import com.cuscatlan.coworking.repository.ReservationRepository;
import com.cuscatlan.coworking.repository.SpaceRepository;
import com.cuscatlan.coworking.service.EmailNotificationService;
import com.cuscatlan.coworking.service.PaymentService;
import com.cuscatlan.coworking.service.ReservationService;
import com.cuscatlan.coworking.service.auth.AuthenticationFacade;
import com.cuscatlan.coworking.service.pricing.PricingStrategy;
import com.cuscatlan.coworking.service.pricing.PricingStrategyFactory;
import com.cuscatlan.coworking.service.reservation.ReservationValidator;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final SpaceRepository spaceRepository;

    private final ReservationMapper reservationMapper;
    private final PaymentMapper paymentMapper;

    private final ReservationValidator reservationValidator;

    private final PricingStrategyFactory pricingStrategyFactory;

    private final PaymentService paymentService;

    private final AuthenticationFacade authenticationFacade;
    
    private final EmailNotificationService emailNotificationService;

    @Override
    public ReservationResponse create(CreateReservationRequest request) {

        User currentUser = authenticationFacade.getCurrentUser();

        Space space = spaceRepository
                .findByIdForUpdate(request.getSpaceId())
                .orElseThrow(() ->
                        new SpaceNotFoundException(request.getSpaceId()));

        reservationValidator.validateCreation(
                currentUser,
                space,
                request);

        PricingStrategy strategy =
                pricingStrategyFactory.getStrategy(space.getType());

        BigDecimal totalPrice =
                strategy.calculatePrice(
                        space,
                        request.getStartDateTime(),
                        request.getEndDateTime());

        PaymentRequest paymentRequest =
                paymentMapper.toPaymentRequest(
                        currentUser,
                        space,
                        totalPrice);

        if (!paymentService.validatePayment(paymentRequest)) {
            throw new PaymentValidationException();
        }

        Reservation reservation = Reservation.builder()
                .user(currentUser)
                .space(space)
                .startDateTime(request.getStartDateTime())
                .endDateTime(request.getEndDateTime())
                .status(ReservationStatus.CONFIRMED)
                .totalPrice(totalPrice)
                .build();

        reservationRepository.save(reservation);
        
        emailNotificationService
        .sendReservationConfirmation(
                reservation);

        return reservationMapper.toResponse(reservation);

    }

    @Override
    @Transactional(readOnly = true)
    public ReservationResponse findById(Long id) {

        Reservation reservation = reservationRepository
                .findDetailedById(id)
                .orElseThrow(() ->
                        new ReservationNotFoundException(id));

        return reservationMapper.toResponse(reservation);

    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationResponse> findAll() {

        return reservationRepository.findAll()
                .stream()
                .map(reservationMapper::toResponse)
                .toList();

    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationResponse> findByCurrentUser() {

        User currentUser = authenticationFacade.getCurrentUser();

        return reservationRepository
                .findByUserId(currentUser.getId())
                .stream()
                .map(reservationMapper::toResponse)
                .toList();

    }

    @Override
    public void cancel(Long reservationId) {

        Reservation reservation = reservationRepository
                .findById(reservationId)
                .orElseThrow(() ->
                        new ReservationNotFoundException(reservationId));

        if (!reservation.getStatus().canBeCancelled()) {
            throw new ReservationCannotBeCancelledException(reservationId);
        }

        reservation.setStatus(ReservationStatus.CANCELLED);

    }

}