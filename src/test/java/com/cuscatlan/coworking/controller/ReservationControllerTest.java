package com.cuscatlan.coworking.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.cuscatlan.coworking.common.util.ApiPaths;
import com.cuscatlan.coworking.dto.request.reservation.CreateReservationRequest;
import com.cuscatlan.coworking.dto.response.reservation.ReservationResponse;
import com.cuscatlan.coworking.enums.ReservationStatus;
import com.cuscatlan.coworking.service.ReservationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.mockito.Mockito.verifyNoInteractions;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReservationService reservationService;

    @Test
    @DisplayName("Should create reservation successfully")
    void shouldCreateReservationSuccessfully() throws Exception {

        CreateReservationRequest request =
                new CreateReservationRequest();

        request.setSpaceId(1L);
        request.setStartDateTime(LocalDateTime.now().plusDays(1));
        request.setEndDateTime(LocalDateTime.now().plusDays(1).plusHours(2));

        ReservationResponse response =
                new ReservationResponse(
                        1L,
                        1L,
                        1L,
                        "Carlos Cerritos",
                        "Sala IT",
                        request.getStartDateTime(),
                        request.getEndDateTime(),
                        ReservationStatus.CONFIRMED,
                        BigDecimal.valueOf(20));

        when(reservationService.create(any()))
                .thenReturn(response);

        mockMvc.perform(post(ApiPaths.RESERVATIONS)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))

                .andExpect(status().isCreated())

                .andExpect(jsonPath("$.success").value(true))

                .andExpect(jsonPath("$.message")
                        .value("Reservation created successfully."))

                .andExpect(jsonPath("$.data.id")
                        .value(1))

                .andExpect(jsonPath("$.data.userName")
                        .value("Carlos Cerritos"))

                .andExpect(jsonPath("$.data.spaceName")
                        .value("Sala IT"));

    }

    @Test
    @DisplayName("Should find reservation by id")
    void shouldFindReservationById() throws Exception {

        ReservationResponse response =
                new ReservationResponse(
                        1L,
                        1L,
                        1L,
                        "Carlos Cerritos",
                        "Sala IT",
                        LocalDateTime.now().plusDays(1),
                        LocalDateTime.now().plusDays(1).plusHours(2),
                        ReservationStatus.CONFIRMED,
                        BigDecimal.valueOf(20));

        when(reservationService.findById(1L))
                .thenReturn(response);

        mockMvc.perform(get(ApiPaths.RESERVATIONS + "/1"))

                .andExpect(status().isOk())

                .andExpect(jsonPath("$.success").value(true))

                .andExpect(jsonPath("$.message")
                        .value("Reservation retrieved successfully."))

                .andExpect(jsonPath("$.data.id")
                        .value(1));

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Should return all reservations")
    void shouldReturnAllReservations() throws Exception {

        ReservationResponse response =
                new ReservationResponse(
                        1L,
                        1L,
                        1L,
                        "Carlos Cerritos",
                        "Sala IT",
                        LocalDateTime.now().plusDays(1),
                        LocalDateTime.now().plusDays(1).plusHours(2),
                        ReservationStatus.CONFIRMED,
                        BigDecimal.valueOf(20));

        when(reservationService.findAll())
                .thenReturn(List.of(response));

        mockMvc.perform(get(ApiPaths.RESERVATIONS))

                .andExpect(status().isOk())

                .andExpect(jsonPath("$.success").value(true))

                .andExpect(jsonPath("$.data.length()")
                        .value(1));

    }

    @Test
    @DisplayName("Should return current user reservations")
    void shouldReturnCurrentUserReservations() throws Exception {

        ReservationResponse response =
                new ReservationResponse(
                        1L,
                        1L,
                        1L,
                        "Carlos Cerritos",
                        "Sala IT",
                        LocalDateTime.now().plusDays(1),
                        LocalDateTime.now().plusDays(1).plusHours(2),
                        ReservationStatus.CONFIRMED,
                        BigDecimal.valueOf(20));

        when(reservationService.findByCurrentUser())
                .thenReturn(List.of(response));

        mockMvc.perform(get(ApiPaths.RESERVATIONS + "/me"))

                .andExpect(status().isOk())

                .andExpect(jsonPath("$.success").value(true))

                .andExpect(jsonPath("$.data.length()")
                        .value(1));

    }

    @Test
    @DisplayName("Should cancel reservation successfully")
    void shouldCancelReservationSuccessfully() throws Exception {

        doNothing()
                .when(reservationService)
                .cancel(1L);

        mockMvc.perform(
                patch(ApiPaths.RESERVATIONS + "/1/cancel"))

                .andExpect(status().isOk())

                .andExpect(jsonPath("$.success").value(true))

                .andExpect(jsonPath("$.message")
                        .value("Reservation cancelled successfully."));

    }
    
    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Should return 403 when USER tries to retrieve all reservations")
    void shouldReturnForbiddenWhenUserRetrievesAllReservations() throws Exception {

        mockMvc.perform(get(ApiPaths.RESERVATIONS))
                .andExpect(status().isForbidden());

        verifyNoInteractions(reservationService);

    }

}