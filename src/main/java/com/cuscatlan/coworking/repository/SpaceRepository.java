package com.cuscatlan.coworking.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
    
    @Query("""
            SELECT new sv.cuscatlan.coworking.dto.response.report.OccupationReportResponse(
                s.id,
                s.name,
                COALESCE(
                    (
                        COUNT(r) * 100.0 /
                        CASE
                            WHEN COUNT(r) = 0 THEN 1
                            ELSE COUNT(r)
                        END
                    ),
                    0
                )
            )
            FROM Space s
            LEFT JOIN s.reservations r
            GROUP BY s.id, s.name
            ORDER BY s.name
            """)
    List<OccupationReportResponse> getOccupationReport();

}