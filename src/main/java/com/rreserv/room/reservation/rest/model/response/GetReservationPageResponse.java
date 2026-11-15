package com.rreserv.room.reservation.rest.model.response;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import lombok.Builder;

/**
 * Response record for Reservation page.
 */
@Builder
public record GetReservationPageResponse(
        Long total,
        List<GetReservationResponse> results) {

    /**
     * Response record for Reservation details. Represents an individual
     * reservation.
     */
    @Builder
    public record GetReservationResponse(
            UUID id,
            String roomName,
            String customerName,
            Instant startDate,
            Instant endDate,
            Boolean canceled) {
    }

}
