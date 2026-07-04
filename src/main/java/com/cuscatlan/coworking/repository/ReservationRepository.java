package com.cuscatlan.coworking.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cuscatlan.coworking.entity.Reservation;
import com.cuscatlan.coworking.entity.Space;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findAll();

    @EntityGraph(attributePaths = {"user", "space"})
    @Query("""
           SELECT r
           FROM Reservation r
           WHERE r.id = :id
           """)
    Optional<Reservation> findDetailedById(@Param("id") Long id);

    List<Reservation> findByUserId(Long userId);

    List<Reservation> findBySpaceId(Long spaceId);

    @Query("""
            SELECT COUNT(r) > 0
            FROM Reservation r
            WHERE r.space.id = :spaceId
              AND r.status <> com.cuscatlan.coworking.enums.ReservationStatus.CANCELLED
              AND r.startDateTime < :endDate
              AND r.endDateTime > :startDate
            """)
    boolean existsOverlappingReservation(
            @Param("spaceId") Long spaceId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

}