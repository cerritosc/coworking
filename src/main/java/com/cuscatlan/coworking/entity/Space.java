package com.cuscatlan.coworking.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.cuscatlan.coworking.enums.SpaceType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "spaces",
        indexes = {
                @Index(name = "idx_space_type", columnList = "type")
        }
)
public class Space extends BaseEntity {

    @Column(nullable = false, length = 120)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SpaceType type;

    @Column(nullable = false)
    private Integer capacity;

    @Column(nullable = false, length = 250)
    private String location;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerHour;

    @Column(nullable = false)
    private Boolean active = true;

    @OneToMany(mappedBy = "space", fetch = FetchType.LAZY)
    private List<Reservation> reservations = new ArrayList<>();

}