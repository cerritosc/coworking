package com.cuscatlan.coworking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.cuscatlan.coworking.entity.Reservation;
import com.cuscatlan.coworking.dto.response.reservation.ReservationResponse;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    @Mapping(target = "userId",
            source = "user.id")

    @Mapping(target = "spaceId",
            source = "space.id")

    @Mapping(target = "userName",
            expression = "java(entity.getUser().getFirstName() + \" \" + entity.getUser().getLastName())")

    @Mapping(target = "spaceName",
            source = "space.name")

    ReservationResponse toResponse(Reservation entity);

}