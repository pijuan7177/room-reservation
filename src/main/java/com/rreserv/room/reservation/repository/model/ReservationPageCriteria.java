package com.rreserv.room.reservation.repository.model;

import java.time.Instant;
import lombok.Builder;

/**
 * Represents filtering criteria for querying Reservations
 */
@Builder 
public record ReservationPageCriteria(    
    String roomName,
    Instant startDate,
    Instant endDate
) {
}
