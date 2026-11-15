package com.rreserv.room.reservation.service.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import lombok.Builder;

/**
 * DTO record for Reservation page.
 */
@Builder
public record GetReservationPageDto(
        Long total,
        List<GetReservationDto> results) {

    /**
     * Represents a DTO for Reservation details
     */
    @Builder
    public record GetReservationDto(
            UUID id,
            String roomName,
            String customerName,
            Instant startDate,
            Instant endDate,
            Boolean canceled) {
    }
}
