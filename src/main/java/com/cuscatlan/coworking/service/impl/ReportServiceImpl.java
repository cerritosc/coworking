package com.cuscatlan.coworking.service.impl;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import com.cuscatlan.coworking.dto.response.report.OccupationReportResponse;
import com.cuscatlan.coworking.entity.Reservation;
import com.cuscatlan.coworking.entity.Space;
import com.cuscatlan.coworking.repository.ReservationRepository;
import com.cuscatlan.coworking.repository.SpaceRepository;
import com.cuscatlan.coworking.service.ReportService;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportServiceImpl implements ReportService {

    private final SpaceRepository spaceRepository;
    
    private final ReservationRepository reservationRepository;

    @Override
    @Cacheable(
            value = "occupation-report",
            key = "#startDate + '-' + #endDate")
    public List<OccupationReportResponse> getOccupationReport(
            LocalDateTime startDate,
            LocalDateTime endDate) {

        if (!endDate.isAfter(startDate)) {
            throw new IllegalArgumentException(
                    "End date must be after start date.");
        }

        List<Reservation> reservations =
                reservationRepository.findReservationsForReport(
                        startDate,
                        endDate);

        List<Space> spaces =
                spaceRepository.findAll();

        double totalHours =
                Duration.between(startDate, endDate).toMinutes() / 60.0;

        Map<Long, Double> reservedHoursBySpace =
                reservations.stream()
                        .collect(Collectors.groupingBy(
                                reservation -> reservation.getSpace().getId(),
                                Collectors.summingDouble(reservation ->
                                        Duration.between(
                                                reservation.getStartDateTime(),
                                                reservation.getEndDateTime())
                                                .toMinutes() / 60.0)));
        
        return spaces.stream()

                .map(space -> {

                	double reservedHours =

                	        reservedHoursBySpace.getOrDefault(
                	                space.getId(),
                	                0.0);

                    double percentage = totalHours == 0

                            ? 0

                            : (reservedHours / totalHours) * 100;

                    return new OccupationReportResponse(

                            space.getId(),

                            space.getName(),

                            Math.round(percentage * 100.0) / 100.0);

                })

                .toList();

    }

}