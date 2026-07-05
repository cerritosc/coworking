package com.cuscatlan.coworking.mapper;

import java.math.BigDecimal;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.cuscatlan.coworking.dto.request.payment.PaymentRequest;
import com.cuscatlan.coworking.entity.Space;
import com.cuscatlan.coworking.entity.User;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "spaceId", source = "space.id")
    @Mapping(target = "amount", source = "amount")
    @Mapping(target = "currency", constant = "USD")
    PaymentRequest toPaymentRequest(
            User user,
            Space space,
            BigDecimal amount);

}