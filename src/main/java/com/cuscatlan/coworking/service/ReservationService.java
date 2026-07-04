package com.cuscatlan.coworking.service;

import java.util.List;

import com.cuscatlan.coworking.dto.request.reservation.CreateReservationRequest;
import com.cuscatlan.coworking.dto.response.reservation.ReservationResponse;

public interface ReservationService {

    ReservationResponse create(CreateReservationRequest request);

    ReservationResponse findById(Long id);

    List<ReservationResponse> findAll();

    List<ReservationResponse> findByCurrentUser();

    void cancel(Long reservationId);

}