package com.cuscatlan.coworking.service.pricing;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.cuscatlan.coworking.entity.Space;
import com.cuscatlan.coworking.enums.SpaceType;

public interface PricingStrategy {

    SpaceType getSupportedType();

    BigDecimal calculatePrice(
            Space space,
            LocalDateTime start,
            LocalDateTime end);

}