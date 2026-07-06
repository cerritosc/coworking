package com.cuscatlan.coworking.service.pricing;

import com.cuscatlan.coworking.entity.Space;
import com.cuscatlan.coworking.support.TestDataFactory;

class DeskPricingStrategyTest
        extends AbstractPricingStrategyTest {

    @Override
    protected PricingStrategy strategy() {

        return new DeskPricingStrategy();

    }

    @Override
    protected Space createSpace() {

        return TestDataFactory.validDesk();

    }

}