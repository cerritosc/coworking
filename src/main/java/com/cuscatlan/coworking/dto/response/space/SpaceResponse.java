package com.cuscatlan.coworking.dto.response.space;

import java.math.BigDecimal;

import com.cuscatlan.coworking.enums.SpaceType;

public record SpaceResponse(

        Long id,

        String name,

        SpaceType type,

        Integer capacity,

        String location,

        BigDecimal pricePerHour,

        Boolean active

) {
}