package com.rreserv.room.reservation.rest.model.response;

import java.util.List;
import java.util.UUID;

import lombok.Builder;

/**
 * Response record for Room list.
 */
@Builder
public record GetRoomListResponse(
        Long total,
        List<GetRoomResponse> results) {

    /**
     * Response record for Room details. Represents an individual room
     */
    @Builder
    public record GetRoomResponse(
            UUID id,
            String name,
            String description,
            Integer capacity,
            Float size) {
    }
}
