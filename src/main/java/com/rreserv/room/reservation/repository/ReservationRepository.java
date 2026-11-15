package com.rreserv.room.reservation.repository;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rreserv.room.reservation.model.Reservation;

/**
 * Repository for managing Reservation entities.
 */
public interface ReservationRepository extends JpaRepository<Reservation, UUID>, ReservationCriteriaRepository {

    @Query("""
            SELECT COUNT(r)
            FROM Reservation r
            WHERE r.room.id = :roomId
              AND r.canceled != TRUE
              AND r.startDate < :endDate
              AND r.endDate > :startDate
            """)
    Long countNotCanceledByRoomIdAndDateRange(
            @Param("roomId") UUID roomId,
            @Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate);

}
