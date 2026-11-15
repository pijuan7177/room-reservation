package com.rreserv.room.reservation.service.dto;

import java.util.List;
import java.util.UUID;

import lombok.Builder;

/**
 * Response record for room list.
 */
@Builder
public record GetRoomListDto(
        Long total,
        List<GetRoomDto> results) {

    /**
     * Represents a DTO for Room details
     */
    @Builder
    public record GetRoomDto(
            UUID id,
            String name,
            String description,
            Integer capacity,
            Float size) {
    }
}
