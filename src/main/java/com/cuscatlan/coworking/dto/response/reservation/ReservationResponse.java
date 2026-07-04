package com.cuscatlan.coworking.dto.response.reservation;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.cuscatlan.coworking.enums.ReservationStatus;

public record ReservationResponse(

        Long id,

        Long userId,

        Long spaceId,

        String userName,

        String spaceName,

        LocalDateTime startDateTime,

        LocalDateTime endDateTime,

        ReservationStatus status,

        BigDecimal totalPrice

) {
}