package com.rreserv.room.reservation.rest.model.response;

import java.util.UUID;

import lombok.Builder;

/**
 * Represents a response for a new room created.
 */
@Builder
public record CreateReservationResponse(
        UUID id
) {
}
