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
import com.cuscatlan.coworking.mapper.ReservationMapper;
import com.cuscatlan.coworking.repository.ReservationRepository;
import com.cuscatlan.coworking.repository.SpaceRepository;
import com.cuscatlan.coworking.service.PaymentService;
import com.cuscatlan.coworking.service.ReservationService;
import com.cuscatlan.coworking.service.auth.AuthenticationService;
import com.cuscatlan.coworking.service.pricing.PricingStrategyFactory;
import com.cuscatlan.coworking.service.reservation.ReservationValidator;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;

    private final SpaceRepository spaceRepository;

    private final AuthenticationService authenticationService;

    private final PricingStrategyFactory pricingStrategyFactory;

    private final ReservationValidator reservationValidator;

    private final ReservationMapper reservationMapper;

    private final PaymentService paymentService;

    @Override
    public ReservationResponse create(CreateReservationRequest request) {

        User user = authenticationService.getCurrentUser();

        Space space = spaceRepository
                .findByIdForUpdate(request.getSpaceId())
                .orElseThrow(() ->
                        new SpaceNotFoundException(request.getSpaceId()));

        reservationValidator.validateCreation(
                user,
                space,
                request);

        BigDecimal totalPrice =
                pricingStrategyFactory
                        .getStrategy(space.getType())
                        .calculatePrice(
                                space,
                                request.getStartDateTime(),
                                request.getEndDateTime());

        PaymentRequest paymentRequest = PaymentRequest.builder()
                .userId(user.getId())
                .spaceId(space.getId())
                .amount(totalPrice)
                .currency("USD")
                .build();

        if (!paymentService.validatePayment(paymentRequest)) {
            throw new PaymentValidationException();
        }

        Reservation reservation = Reservation.builder()
                .user(user)
                .space(space)
                .startDateTime(request.getStartDateTime())
                .endDateTime(request.getEndDateTime())
                .status(ReservationStatus.CONFIRMED)
                .totalPrice(totalPrice)
                .build();

        reservationRepository.save(reservation);

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

        User currentUser = authenticationService.getCurrentUser();

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
            throw new ReservationCannotBeCancelledException(reservation.getId());
        }

        reservation.setStatus(ReservationStatus.CANCELLED);

        reservationRepository.save(reservation);

    }

}