package com.cuscatlan.coworking.service.pricing;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.cuscatlan.coworking.common.exception.UnsupportedSpaceTypeException;
import com.cuscatlan.coworking.enums.SpaceType;

@Component
public class PricingStrategyFactory {

    private final Map<SpaceType, PricingStrategy> strategies;

    public PricingStrategyFactory(List<PricingStrategy> strategyList) {

        this.strategies = new EnumMap<>(SpaceType.class);

        strategyList.forEach(strategy ->
                strategies.put(strategy.getSupportedType(), strategy));

    }

    public PricingStrategy getStrategy(SpaceType spaceType) {

        PricingStrategy strategy = strategies.get(spaceType);

        if (strategy == null) {
            throw new UnsupportedSpaceTypeException(spaceType);
        }

        return strategy;
    }

}