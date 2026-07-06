package com.cuscatlan.coworking.service.pricing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.cuscatlan.coworking.entity.Space;
import com.cuscatlan.coworking.support.TestDataFactory;

public abstract class AbstractPricingStrategyTest {

    protected abstract PricingStrategy strategy();

    protected abstract Space createSpace();

    @Test
    void shouldCalculateOneHourPrice() {

        Space space = createSpace();

        LocalDateTime start = LocalDateTime.of(
                2026,
                1,
                1,
                8,
                0);

        LocalDateTime end = start.plusHours(1);

        BigDecimal price =
                strategy().calculatePrice(
                        space,
                        start,
                        end);

        assertEquals(
                BigDecimal.TEN,
                price);

    }

    @Test
    void shouldCalculateTwoHoursPrice() {

        Space space = createSpace();

        LocalDateTime start = LocalDateTime.of(
                2026,
                1,
                1,
                8,
                0);

        LocalDateTime end = start.plusHours(2);

        BigDecimal price =
                strategy().calculatePrice(
                        space,
                        start,
                        end);

        assertEquals(
                BigDecimal.valueOf(20),
                price);

    }

}