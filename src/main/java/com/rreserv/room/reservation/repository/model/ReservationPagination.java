package com.rreserv.room.reservation.repository.model;

import lombok.Builder;

/**
 * Record representing pagination parameters.
 */
@Builder 
public record ReservationPagination(
    int offset,
    int limit,
    ReservationSortBy sortBy,
    SortDirection sortDir
) {
}
