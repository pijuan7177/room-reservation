package com.rreserv.room.reservation.rest.model.request;

import jakarta.validation.constraints.NotBlank;

/**
 * Represents a request to user login.
 */
public record LoginRequest(
        
        @NotBlank 
        String username,
        
        @NotBlank 
        String password
) {
}
