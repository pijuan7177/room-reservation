package com.rreserv.room.reservation.repository.model;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import lombok.Builder;

/** 
 * Record for ReservationPage
 */
@Builder
public record ReservationPage(
        Long total,
        List<Reservation> results) {

    /**
     * Represents a Reservation details
     */
    @Builder
    public record Reservation(
            UUID id,
            String roomName,
            String customerName,
            Instant startDate,
            Instant endDate,
            Boolean canceled) {
    }

}
