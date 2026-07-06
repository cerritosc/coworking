package com.cuscatlan.coworking.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cuscatlan.coworking.dto.response.report.OccupationReportProjection;
import com.cuscatlan.coworking.dto.response.report.OccupationReportResponse;
import com.cuscatlan.coworking.entity.Space;
import com.cuscatlan.coworking.enums.SpaceType;

import jakarta.persistence.LockModeType;

public interface SpaceRepository extends
        JpaRepository<Space, Long>,
        JpaSpecificationExecutor<Space> {

    List<Space> findAll();

    List<Space> findByActiveTrue();

    List<Space> findByTypeAndActiveTrue(SpaceType type);
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
           SELECT s
           FROM Space s
           WHERE s.id = :id
           """)
    Optional<Space> findByIdForUpdate(@Param("id") Long id);
    
    @EntityGraph(attributePaths = "reservations")
    Optional<Space> findWithReservationsById(Long id);

}