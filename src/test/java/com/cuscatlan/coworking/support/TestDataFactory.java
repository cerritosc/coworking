package com.cuscatlan.coworking.support;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.cuscatlan.coworking.dto.request.auth.AuthenticationRequest;
import com.cuscatlan.coworking.dto.request.auth.RegisterRequest;
import com.cuscatlan.coworking.dto.request.reservation.CreateReservationRequest;
import com.cuscatlan.coworking.dto.response.user.UserResponse;
import com.cuscatlan.coworking.entity.Space;
import com.cuscatlan.coworking.entity.User;
import com.cuscatlan.coworking.enums.Role;
import com.cuscatlan.coworking.enums.SpaceType;

public final class TestDataFactory {

    private TestDataFactory() {
    }

    public static User validUser() {

        return User.builder()
                .id(TestConstants.USER_ID)
                .firstName(TestConstants.FIRST_NAME)
                .lastName(TestConstants.LAST_NAME)
                .email(TestConstants.EMAIL)
                .password(TestConstants.PASSWORD)
                .role(Role.USER)
                .enabled(true)
                .build();

    }

    public static Space validMeetingRoom() {

        return Space.builder()
                .id(TestConstants.SPACE_ID)
                .name("Meeting Room")
                .type(SpaceType.MEETING_ROOM)
                .capacity(10)
                .location("Floor 5")
                .pricePerHour(TestConstants.PRICE_PER_HOUR)
                .active(true)
                .build();

    }

    public static CreateReservationRequest validReservationRequest() {

        CreateReservationRequest request =
                new CreateReservationRequest();

        request.setSpaceId(TestConstants.SPACE_ID);

        request.setStartDateTime(
                LocalDateTime.now().plusHours(1));

        request.setEndDateTime(
                LocalDateTime.now().plusHours(2));

        return request;

    }
    
    public static Space validDesk() {

        return Space.builder()
                .id(TestConstants.SPACE_ID)
                .name("Desk")
                .type(SpaceType.DESK)
                .capacity(1)
                .location("Floor 1")
                .pricePerHour(BigDecimal.TEN)
                .active(true)
                .build();

    }

    public static Space validPrivateOffice() {

        return Space.builder()
                .id(TestConstants.SPACE_ID)
                .name("Private Office")
                .type(SpaceType.PRIVATE_OFFICE)
                .capacity(4)
                .location("Floor 2")
                .pricePerHour(BigDecimal.TEN)
                .active(true)
                .build();

    }
    
    public static RegisterRequest validRegisterRequest() {

        RegisterRequest request = new RegisterRequest();

        request.setFirstName("Carlos");
        request.setLastName("Cerritos");
        request.setEmail("carlos@test.com");
        request.setPassword("Password123");

        return request;

    }
    
    public static AuthenticationRequest validAuthenticationRequest() {

        return new AuthenticationRequest(
                "carlos@test.com",
                "Password123");

    }
    
    public static UserResponse validUserResponse() {

        return new UserResponse(
                1L,
                "Carlos",
                "Cerritos",
                "carlos@test.com",
                Role.USER);

    }

}