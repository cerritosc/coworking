package com.cuscatlan.coworking.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.cuscatlan.coworking.common.util.*;
import com.cuscatlan.coworking.common.response.ApiResponse;
import com.cuscatlan.coworking.dto.request.reservation.CreateReservationRequest;
import com.cuscatlan.coworking.dto.response.reservation.ReservationResponse;
import com.cuscatlan.coworking.service.ReservationService;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPaths.RESERVATIONS)
@Tag(
        name = "Reservations",
        description = "Operations related to coworking reservations"
)
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    @Operation(summary = "Create a reservation")
    public ResponseEntity<ApiResponse<ReservationResponse>> create(
            @Valid @RequestBody CreateReservationRequest request) {

        ReservationResponse response =
                reservationService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Reservation created successfully.",
                        response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find reservation by id")
    public ResponseEntity<ApiResponse<ReservationResponse>> findById(
            @PathVariable Long id) {

        ReservationResponse response =
                reservationService.findById(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Reservation retrieved successfully.",
                        response));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    @Operation(summary = "Get all reservations")
    public ResponseEntity<ApiResponse<List<ReservationResponse>>> findAll() {

        List<ReservationResponse> response =
                reservationService.findAll();

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Reservations retrieved successfully.",
                        response));
    }

    @GetMapping("/me")
    @Operation(summary = "Get current user's reservations")
    public ResponseEntity<ApiResponse<List<ReservationResponse>>> findMyReservations() {

        List<ReservationResponse> response =
                reservationService.findByCurrentUser();

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Reservations retrieved successfully.",
                        response));
    }

    @PatchMapping("/{id}/cancel")
    @Operation(summary = "Cancel reservation")
    public ResponseEntity<ApiResponse<Void>> cancel(
            @PathVariable Long id) {

        reservationService.cancel(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Reservation cancelled successfully.",
                        null));
    }

}