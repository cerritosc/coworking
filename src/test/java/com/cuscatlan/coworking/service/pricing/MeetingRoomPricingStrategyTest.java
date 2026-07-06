package com.cuscatlan.coworking.service.pricing;

import com.cuscatlan.coworking.entity.Space;
import com.cuscatlan.coworking.support.TestDataFactory;

class MeetingRoomPricingStrategyTest
        extends AbstractPricingStrategyTest {

    @Override
    protected PricingStrategy strategy() {

        return new MeetingRoomPricingStrategy();

    }

    @Override
    protected Space createSpace() {

        return TestDataFactory.validMeetingRoom();

    }

}