package com.rreserv.room.reservation.rest.model.response;

import lombok.Builder;

/**
 * Represents a response for user login.
 */
@Builder 
public record LoginResponse(
        String accessToken,
        String tokenType,
        long expiresInSeconds
) {
}
