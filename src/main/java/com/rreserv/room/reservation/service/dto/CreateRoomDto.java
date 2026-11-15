package com.rreserv.room.reservation.service.dto;

import lombok.Builder;

/**
 * Represents a DTO for room creation
 */
@Builder 
public record CreateRoomDto(
    String name,
    String description,
    Integer capacity,
    Float size
) {
}
