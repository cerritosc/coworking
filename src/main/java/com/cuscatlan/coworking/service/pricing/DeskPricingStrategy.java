package com.cuscatlan.coworking.service.pricing;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.cuscatlan.coworking.entity.Space;
import com.cuscatlan.coworking.enums.SpaceType;

@Service
public class DeskPricingStrategy implements PricingStrategy {

    @Override
    public SpaceType getSupportedType() {
        return SpaceType.DESK;
    }

    @Override
    public BigDecimal calculatePrice(
            Space space,
            LocalDateTime start,
            LocalDateTime end) {

        long hours = Duration.between(start, end).toHours();

        return space.getPricePerHour()
                .multiply(BigDecimal.valueOf(hours));
    }

}