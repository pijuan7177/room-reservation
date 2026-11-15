package com.rreserv.room.reservation.service.dto;

import java.time.Instant;
import java.util.UUID;

import lombok.Builder;

/**
 * Represents a DTO for reservation creation
 */
@Builder
public record CreateReservationDto(
        String customerName,
        Instant startDate,
        Instant endDate,
        UUID roomId
) {
}
