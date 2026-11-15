package com.rreserv.room.reservation.service.dto;

import java.time.Instant;
import lombok.Builder;

/**
 * Represents filtering criteria for querying Reservations in the service layer.
 */
@Builder
public record GetReservationFilters(
    String roomName,
    Instant startDate,
    Instant endDate
) {
}
