package com.cuscatlan.coworking.service.pricing;

import com.cuscatlan.coworking.entity.Space;
import com.cuscatlan.coworking.support.TestDataFactory;

class PrivateOfficePricingStrategyTest
        extends AbstractPricingStrategyTest {

    @Override
    protected PricingStrategy strategy() {

        return new PrivateOfficePricingStrategy();

    }

    @Override
    protected Space createSpace() {

        return TestDataFactory.validPrivateOffice();

    }

}