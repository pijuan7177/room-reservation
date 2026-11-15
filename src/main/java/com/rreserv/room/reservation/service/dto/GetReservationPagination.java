package com.rreserv.room.reservation.service.dto;

import lombok.Builder;

/**
 * Record representing pagination parameters.
 */
@Builder
public record GetReservationPagination(
    int offset,
    int limit,
    GetReservationSortBy sortBy,
    SortDirection sortDir
) {
}
