package com.cuscatlan.coworking.dto.request.space;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import com.cuscatlan.coworking.enums.SpaceType;

@Getter
@Setter
public class CreateSpaceRequest {

    @NotBlank
    private String name;

    @NotNull
    private SpaceType type;

    @Min(1)
    private Integer capacity;

    @NotBlank
    private String location;

    @NotNull
    @DecimalMin("0.00")
    private BigDecimal pricePerHour;

}